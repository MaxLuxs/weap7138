package luxs.max.weap7138.UI

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import luxs.max.weap7138.Model.LocalModel.LocalModel
import luxs.max.weap7138.Model.RemoteModel.ApiService
import luxs.max.weap7138.Model.RemoteModel.RemoteModel
import luxs.max.weap7138.R
import luxs.max.weap7138.Repository.WeatherRepository
import luxs.max.weap7138.Services.UpdateService
import luxs.max.weap7138.ViewModel.WeatherViewModel
import luxs.max.weap7138.WeatherData
import java.text.DateFormat
import java.text.DecimalFormat
import java.util.*
/**Main activity
 * Only activity_main.xml
 * Data:
 *      apiService -> to get data from community-open-weather-map.p.rapidapi.com/
 *      weather data -> data class
 *
 * Menu 2 item : 1.Change City, 2.Update
 *
 * Updating the view is done primitively -> in one method : downloadWeatherDataAndUpdateView().
 *
 * Maksim Lukashevich
 * */
class MainActivity : AppCompatActivity() {

    private val apiService = ApiService.create()
    lateinit var weatherData: WeatherData//Deprecated

    lateinit var cityName:String

    private lateinit var weatherViewModel:WeatherViewModel

    /**in the main activity, when creating,
     * a toast is displayed with advice for updating,
     * the data is downloaded and the handler for clicking on the icon is installed.*/
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(applicationContext, "Click on the picture to update", Toast.LENGTH_LONG).show()
        cityName = "Minsk"
        //Dependency injection: ^_^ where is my dagger
        val remoteModel = RemoteModel()
        val localModel = LocalModel()
        val repository = WeatherRepository(remoteModel, localModel, cityName)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        weatherViewModel.repository = repository // Before We are going to create Beautiful
        weatherViewModel.cityWeatherLiveData.value = null

//        downloadWeatherDataAndUpdateView(cityName)//Deprecated
        weatherViewModel.getData()

        weatherIcon.setOnClickListener(View.OnClickListener {
//            downloadWeatherDataAndUpdateView(cityName)//Deprecated
            weatherViewModel.getData()
        })

        weatherViewModel.cityWeatherLiveData.observe(this, androidx.lifecycle.Observer {
            val weatherData = weatherViewModel.cityWeatherLiveData.value
            Log.d("!!!Data: ", weatherData.toString())
            if (weatherData == null){
                temperatureTW.text = "No data =(" //<---------------------replace as time will be
            }else{
                lastUpdateTW.text = DateFormat.getDateTimeInstance().format(Date())
                cityNameTW.text = weatherData.name

                Picasso.get().load(
                    "http://openweathermap.org/img/wn/" +
                            weatherData.weather[0].icon + "@2x" + ".png"
                )
                    .fit()
                    .centerCrop()
                    .into(weatherIcon)
                weatherMainTW.text = weatherData.weather[0].main
                weatherDescriptionTW.text = weatherData.weather[0].description

                humidityTW.text = "Humidity: " +
                        weatherData.main.humidity.toString() + " %"
                pressureTW.text = "Pressure: " +
                        weatherData.main.pressure.toString() + " hPa"
                temperatureTW.text =
                    DecimalFormat("##.#").format(weatherData.main.temp - 273.16)
                        .toString() + "°C"
                maxTempTW.text = "Temp max: " +
                        DecimalFormat("##.#")
                            .format(weatherData.main.temp_max - 273.16).toString() + "°C"
                minTempTW.text = "Temp min: " +
                        DecimalFormat("##.#")
                            .format(weatherData.main.temp_min - 273.16).toString() + "°C"
                progressBar.visibility = View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //Пункт меню 1
            R.id.changeCityI -> {
                val et = EditText(this)
                val alertDialog = AlertDialog.Builder(this)
                alertDialog
                    .setTitle("Enter city name")
                    .setView(et)
                    .setPositiveButton("Yes") { dialog, _ ->
                        if (et.text.isEmpty()) {
                            Toast.makeText(
                                this,
                                "You did not enter a city name",
                                Toast.LENGTH_SHORT
                            ).show();
                        } else {
                            cityName = et.text.toString()
                            item.title = "Change city name: $cityName"
                            dialog.cancel()
                            Toast.makeText(
                                this,
                                "Please click update or icon",
                                Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                    .create()
                alertDialog.show()
            }
            //Пункт меню 2
            R.id.updateI -> {
                weatherViewModel.getData()
//                downloadWeatherDataAndUpdateView(cityName)
            }
            //Пункт меню 3 -> Обновление в сервисе каждые 10 секунд -> Только для проверки
            R.id.updateEveryTenSec -> {
                if (item.title == "Update every ten sec: Off") {
                    item.title = "Update every ten sec: On"
                    // Bind to LocalService
                    Intent(this, UpdateService::class.java).also { intent ->
                        startService(intent)
                    }
                }else{
                    // Bind to LocalService
                    Intent(this, UpdateService::class.java).also { intent ->
                        stopService(intent)
                    }
                    item.title = "Update every ten sec: Off"
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Deprecated!------------------------
    @SuppressLint("SetTextI18n")
    fun downloadWeatherDataAndUpdateView(cityName:String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                progressBar.visibility = View.VISIBLE

                weatherData = apiService.getWeather(cityName)
                Log.d("!!!Data: ", weatherData.toString())
                lastUpdateTW.text = DateFormat.getDateTimeInstance().format(Date())
                cityNameTW.text = weatherData.name

                Picasso.get().load(
                    "http://openweathermap.org/img/wn/" +
                            weatherData.weather[0].icon + "@2x" + ".png"
                )
                    .fit()
                    .centerCrop()
                    .into(weatherIcon)
                weatherMainTW.text = weatherData.weather[0].main
                weatherDescriptionTW.text = weatherData.weather[0].description

                humidityTW.text = "Humidity: " +
                        weatherData.main.humidity.toString() + " %"
                pressureTW.text = "Pressure: " +
                        weatherData.main.pressure.toString() + " hPa"
                temperatureTW.text =
                    DecimalFormat("##.#").format(weatherData.main.temp - 273.16)
                        .toString() + "°C"
                maxTempTW.text = "Temp max: " +
                        DecimalFormat("##.#")
                            .format(weatherData.main.temp_max - 273.16).toString() + "°C"
                minTempTW.text = "Temp min: " +
                        DecimalFormat("##.#")
                            .format(weatherData.main.temp_min - 273.16).toString() + "°C"
                progressBar.visibility = View.GONE

            }catch(e : retrofit2.HttpException) {
                Log.e("!!!HTTPError", e.toString())
                //gag^_^
                if (e.code() != 404){
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(applicationContext,
                        "Please go to settings and change the city name to the correct one.",
                        Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception) {
                    Log.e("!!!Error", e.toString())
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

}
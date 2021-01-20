package luxs.max.weap7138

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    lateinit var weatherData:WeatherData

    lateinit var cityName:String
/*
* asda
* da
* */
    /**in the main activity, when creating,
     * a toast is displayed with advice for updating,
     * the data is downloaded and the handler for clicking on the icon is installed.*/
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(applicationContext, "Click on the picture to update", Toast.LENGTH_LONG).show()
        cityName = "Minsk"

        downloadWeatherDataAndUpdateView(cityName)

        weatherIcon.setOnClickListener(View.OnClickListener {
            downloadWeatherDataAndUpdateView(cityName)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
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
            R.id.updateI -> {
                downloadWeatherDataAndUpdateView(cityName)
            }
        }
        return super.onOptionsItemSelected(item)
    }

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
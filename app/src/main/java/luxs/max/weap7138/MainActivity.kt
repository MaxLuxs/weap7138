package luxs.max.weap7138

import android.annotation.SuppressLint
import android.content.DialogInterface
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
import kotlinx.android.synthetic.main.enter_city_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.DecimalFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val apiService = ApiService.create()
    lateinit var weatherData:WeatherData

    lateinit var cityName:String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(applicationContext, "Click on the picture to update", Toast.LENGTH_LONG).show()
        cityName = "Minsk"

        weatherIcon.setOnClickListener(View.OnClickListener {
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

                } catch (e: Exception) {
                    Log.e("!!!Error", e.toString())
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
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
                            dialog.cancel()
                        }
                    }
                    .create()
                alertDialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
package luxs.max.weap7138

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private val apiService = ApiService.create()
    lateinit var weatherData:WeatherData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(applicationContext, "Click on the picture to update", Toast.LENGTH_LONG).show()

        weatherIcon.setOnClickListener(View.OnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    progressBar.visibility = View.VISIBLE

                    weatherData = apiService.getWeather()
                    Log.d("!!!Data: ", weatherData.toString())
                    lastUpdateTW.text = "just"
                    cityNameTW.text = weatherData.name
                    humidityTW.text = weatherData.main.humidity.toString() + " %"
                    pressureTW.text = weatherData.main.pressure.toString() + " hPa"
                    temperatureTW.text = DecimalFormat("##.#").format(weatherData.main.temp - 273).toString() + "°C"

                    progressBar.visibility = View.GONE

                }catch (e:Exception){
                    Log.e("!!!Error", e.toString())
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

}
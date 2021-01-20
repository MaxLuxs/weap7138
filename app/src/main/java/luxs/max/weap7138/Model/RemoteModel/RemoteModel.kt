package luxs.max.weap7138.Model.RemoteModel

import android.util.Log
import android.widget.Toast
import luxs.max.weap7138.WeatherData
import java.lang.Exception

class RemoteModel {
    private val apiService = ApiService.create()

    suspend fun getWeatherRemoteData(cityName:String):WeatherData?{
        return try {
            apiService.getWeather(cityName)
        }catch(e : retrofit2.HttpException) {
            Log.e("!!!HTTPError", e.toString())
            null
        }catch (e: Exception) {
            Log.e("!!!Error", e.toString())
            null
        }
    }
}
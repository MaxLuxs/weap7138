package luxs.max.weap7138

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://community-open-weather-map.p.rapidapi.com/"

interface ApiService{
    @Headers("x-rapidapi-key: 428dc8cd86mshb5771208a2f96b7p1298abjsn6f89d16e8d94",
        "x-rapidapi-host: community-open-weather-map.p.rapidapi.com")
    @GET("weather?")
    suspend fun getWeather(@Query("q") cityName:String):WeatherData

    companion object Factory{
        fun create():ApiService{
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}

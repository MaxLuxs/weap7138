package luxs.max.weap7138.Repository

import luxs.max.weap7138.Model.LocalModel.LocalModel
import luxs.max.weap7138.Model.RemoteModel.RemoteModel
import luxs.max.weap7138.WeatherData

class WeatherRepository(private val remoteModel: RemoteModel, private val localModel:LocalModel, private var cityName:String) {

    suspend fun getData():WeatherData?{
        return remoteModel.getWeatherRemoteData(cityName)
    }

}
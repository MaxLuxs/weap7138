package luxs.max.weap7138.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import luxs.max.weap7138.Repository.WeatherRepository
import luxs.max.weap7138.WeatherData
import java.io.Serializable

class WeatherViewModel : ViewModel() {
    lateinit var repository: WeatherRepository
    private val scope = CoroutineScope(Dispatchers.IO)

    val cityWeatherLiveData: MutableLiveData<WeatherData> by lazy {
        MutableLiveData<WeatherData>()
    }

    fun getData(){
        scope.launch {
            cityWeatherLiveData.postValue(repository.getData())
        }
    }

}
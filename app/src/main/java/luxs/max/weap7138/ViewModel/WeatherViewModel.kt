package luxs.max.weap7138.ViewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import luxs.max.weap7138.Repository.WeatherRepository
import luxs.max.weap7138.Services.UpdateWorker
import luxs.max.weap7138.WeatherData
import java.io.Serializable

const val DOWNLOAD_MANIPULATION = "download work"

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var repository: WeatherRepository
    private val scope = CoroutineScope(Dispatchers.IO)

    val cityNameLiveData:MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val cityWeatherLiveData: MutableLiveData<WeatherData> by lazy {
        MutableLiveData<WeatherData>()
    }

    @SuppressLint("EnqueueWork")
    fun getData(){
        scope.launch {
            cityWeatherLiveData.postValue(repository.getData())
        }
    }

}
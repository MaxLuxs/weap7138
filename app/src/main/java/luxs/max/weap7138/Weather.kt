package luxs.max.weap7138

/**Models kotlin file*/

data class WeatherData(
    val coord:Coord,
    val weather:List<Weather>,
    val base:String,
    val main:MainWeatherData,
    val visibility:Int,
    val wind:Wind,
    val clouds:Clouds,
    val dt:Int,
    val sys:Sys,
    val timezone:Int,
    val id:Int,
    val name:String,
    val cod:Int
)

data class Coord(
    val lon:Double,
    val lat:Double
)

data class Weather(
    val id:Int,
    val main:String,
    val description:String,
    val icon:String
)

data class MainWeatherData(
    val temp:Double,
    val pressure:Int,
    val humidity:Int,
    val temp_min:Double,
    val temp_max:Double
)

data class Wind(
    val speed:Double
)

data class Clouds(
    val all:Int
)

data class Sys(
    val type:Int,
    val id:Int,
    val message:Double,
    val country:String,
    val sunrise:Int,
    val sunset:Int
)
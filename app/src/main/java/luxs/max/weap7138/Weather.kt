package luxs.max.weap7138

/**Models:*/
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
    val tempmin:Double,
    val tempmax:Double
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


/**test (
 * test(
 *  {
 *      "coord":{
 *          "lon":-0.13,
 *          "lat":51.51
 *          },
 *      "weather":[
 *          {"id":741,
 *          "main":"Fog",
 *          "description":"fog",
 *          "icon":"50n"}
 *         ],
 *      "base":"stations",
 *      "main":{
 *          "temp":284.04,
 *          "pressure":1011,
 *          "humidity":93,
 *          "tempmin":280.93,
 *          "tempmax":287.04
 *         },
 *      "visibility":10000,
 *      "wind":{
 *          "speed":1.5
 *          },
 *       "clouds":{
 *              "all":20
 *           },
 *      "dt":1570234102,
 *      "sys":{
 *          "type":1,
 *          "id":1417,
 *          "message":0.0102,
 *          "country":"GB",
 *          "sunrise":1570255614,
 *          "sunset":1570296659
 *         },
 *      "timezone":3600,
 *      "id":2643743,
 *      "name":"London",
 *      "cod":200
 *  }
 * )*/
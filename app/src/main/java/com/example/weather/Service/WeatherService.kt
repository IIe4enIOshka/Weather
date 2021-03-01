package com.example.weather.Service

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.Entity.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat
import java.util.*

class WeatherService {
//    private val citys = mutableListOf(
//        City(id = 0, name = "Лондон", temp = 12, temp2 = 12, temp3 = 12),
//        City(id = 1, name = "Москва", temp = 12, temp2 = 12, temp3 = 12),
//        City(id = 2, name = "Новосибирск", temp = 12, temp2 = 12, temp3 = 12),
//        City(id = 3, name = "Киев", temp = 12, temp2 = 12, temp3 = 12),
//        City(id = 4, name = "Томск", temp = 12, temp2 = 12, temp3 = 12),
//        City(id = 5, name = "Омск", temp = 12, temp2 = 12, temp3 = 12)
//    )

    var citys = mutableListOf<City>()
    val listUrls = mutableListOf(
        URL("http://api.openweathermap.org/data/2.5/find?q=London&appid=fb9ac05d0fbe1a2f35e1ff6a57aa4d72"),
        URL("http://api.openweathermap.org/data/2.5/find?q=Berlin&appid=fb9ac05d0fbe1a2f35e1ff6a57aa4d72"),
        URL("http://api.openweathermap.org/data/2.5/find?q=Paris&appid=fb9ac05d0fbe1a2f35e1ff6a57aa4d72"),
        URL("http://api.openweathermap.org/data/2.5/find?q=Dallas&appid=fb9ac05d0fbe1a2f35e1ff6a57aa4d72"),
        URL("http://api.openweathermap.org/data/2.5/find?q=panama&appid=fb9ac05d0fbe1a2f35e1ff6a57aa4d72"),
    )

    fun getCity(): List<City> = citys

    fun getCity(id: Int, citys2: List<City>): City? = citys2.firstOrNull { it.id == id }

    fun setCity(city: City) {
        val editedPersonIndex = citys.indexOfFirst { it.id == city.id }
        if (editedPersonIndex >= 0) {
            citys[editedPersonIndex] = city
        }
    }

    fun getData(): List<City> {
        citys.clear()
        var name = ""
        val listTemp = mutableListOf<Double>()
        try {
            for (index in 0 until listUrls.size) {
                listTemp.clear()
                val urlConnection = listUrls[index].openConnection() as HttpURLConnection

                try {
                    val `in` = urlConnection.inputStream
                    val scan = Scanner(`in`)
                    scan.useDelimiter("\\A")
                    val jsonArray = JSONObject(scan.next()).getJSONArray("list")
                    val df = DecimalFormat("##")
                    name = jsonArray.getJSONObject(0).getString("name").toString()
                    for (i in 0 until 3) {
                        listTemp.add(
                            jsonArray.getJSONObject(i).getJSONObject("main").getString("temp")
                                .toDouble()
                        )
                    }
                    citys.add(
                        City(
                            index, name,
                            df.format(listTemp[0] - 273.15).toInt(),
                            df.format(listTemp[1] - 273.15).toInt(),
                            df.format(listTemp[2] - 273.15).toInt(),
                        )
                    )
                } catch (e: Exception) {
                    println(e.message)
                } finally {
                    urlConnection.disconnect()
                }
            }
        } catch (e: IOException) {
            Log.d("Error", e.message.toString());
        }
        return citys
    }
}
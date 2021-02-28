package com.example.weather.List

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.CityAdapter
import com.example.weather.Detail.DetailActivity
import com.example.weather.Entity.City
import com.example.weather.R
import com.example.weather.Service.WeatherService
import com.example.weather.WeatherApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class ListActivity : AppCompatActivity() {

    private lateinit var weatherService: WeatherService

    //lateinit var sharededPreferences: SharedPreferences
    private lateinit var cityList: RecyclerView

    private val adapter = CityAdapter {
        DetailActivity.start(this, it.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherService = (application as WeatherApplication).weatherService
        setContentView(R.layout.activity_main)

        cityList = findViewById(R.id.cityList)
        cityList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.citys = weatherService.getCity()

        if (adapter.citys.isNotEmpty()) {
            println("fdffd")
            GlobalScope.launch() {
                try {
                    val city = weatherService.getData()
                    withContext(Dispatchers.Main) {
                        adapter.citys = city
                        saveData(adapter.citys)
                    }
                } catch (e: Exception) {
                    Log.d("Error", e.message.toString())
                }
            }
        } else {
            GlobalScope.launch() {
                try {
                    val city = loadData()
                    withContext(Dispatchers.Main) {
                        adapter.citys = city
                    }
                } catch (e: Exception) {
                    Log.d("Error", e.message.toString())
                }
            }
        }
    }

    fun saveData(list: List<City>) {
        val sharedPreferences = getSharedPreferences("shared pref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val mass = JSONArray()
        for(i in list.indices)
        {
            val eachData = JSONObject()
            mass.put(i, eachData.put("id",list[i].id))
            mass.put(i, eachData.put("name",list[i].name))
            mass.put(i, eachData.put("temp",list[i].temp))
            mass.put(i, eachData.put("temp2",list[i].temp2))
            mass.put(i, eachData.put("temp3",list[i].temp3))
        }
        editor.putString("Data", mass.toString())
        editor.apply()
    }

    fun loadData(): List<City> {
        val listCitys = mutableListOf<City>()
        val sharedPreferences = getSharedPreferences("shared pref", MODE_PRIVATE)
        val data = sharedPreferences.getString("Data", null)
        val json = JSONArray(data)
        for (i in 0 until json.length())
        {
            val id = json.getJSONObject(i).getString("id")
            val name = json.getJSONObject(i).getString("name")
            val temp = json.getJSONObject(i).getString("temp").toInt()
            val temp2 = json.getJSONObject(i).getString("temp2").toInt()
            val temp3 = json.getJSONObject(i).getString("temp3").toInt()
            listCitys.add(City(i, name, temp, temp2, temp3))
        }
        return listCitys
    }

}
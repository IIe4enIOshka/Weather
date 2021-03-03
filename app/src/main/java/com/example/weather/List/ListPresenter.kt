package com.example.weather.List

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.BasePresenter
import com.example.weather.Entity.City
import com.example.weather.Service.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ListPresenter(private val service: WeatherService) : BasePresenter<ListView>() {

    lateinit var citysList: List<City>

    fun onViewResumed() {
        citysList = service.getCity()
        println(citysList)
        if (citysList.isEmpty()) {
            try{
                citysList = view?.loadData()!!
            }catch (e: java.lang.Exception)
            {
                println(e.message)
            }
            println(citysList)
            if (citysList.isEmpty()) {
                GlobalScope.launch() {
                    try {
                        val city = service.getData()
                        println("city")
                        withContext(Dispatchers.Main) {
                            citysList = city
                            println(city)
                            println(citysList)
                            view?.bindCityList(citysList)
                            view?.saveData(citysList)
                            view?.getDate()
                        }
                    } catch (e: Exception) {
                        Log.d("Error", e.message.toString())
                    }
                }
            }
        }
        view?.getDate()
        view?.bindCityList(citysList)
    }

    fun onCityClicked(city: City) {
        view?.openCityDetailsScreen(city.id)
    }

    fun onViewUpdate() {
        GlobalScope.launch() {
            try {
                val city = service.getData()
                withContext(Dispatchers.Main) {
                    citysList = city
                    view?.saveData(citysList)
                    view?.getDate()
                    view?.bindCityList(citysList)
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
        }

    }
}
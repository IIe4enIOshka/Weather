package com.example.weather

import android.app.Application
import com.example.weather.Repo.WeatherRepo
import com.example.weather.Service.WeatherService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherApplication : Application() {

    lateinit var weatherService: WeatherService

    override fun onCreate() {
        super.onCreate()
        weatherService = WeatherService()
    }
}
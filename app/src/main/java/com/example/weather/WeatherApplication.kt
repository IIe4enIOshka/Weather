package com.example.weather

import android.app.Application
import com.example.weather.Service.WeatherService


class WeatherApplication : Application() {

    lateinit var weatherService: WeatherService

    override fun onCreate() {
        super.onCreate()
        weatherService = WeatherService()
    }
}
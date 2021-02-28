package com.example.weather

import android.os.Build
import java.net.URL

class Config {
    private val url = "http://api.openweathermap.org/data/2.5/box/city?bbox=12,32,15,37,10&appid=fb9ac05d0fbe1a2f35e1ff6a57aa4d72";

    fun getUrl(){
        val jsonStr = URL(url).readText()
    }
}
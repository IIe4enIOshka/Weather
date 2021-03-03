package com.example.weather.Detail

import com.example.weather.BaseView
import com.example.weather.Entity.City

interface DetailView : BaseView {

    fun bindCity(city: City)

    fun closeScreen()
}
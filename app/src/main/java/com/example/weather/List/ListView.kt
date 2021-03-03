package com.example.weather.List

import com.example.weather.BaseView
import com.example.weather.Entity.City

interface ListView : BaseView {
    fun bindCityList(list: List<City>)

    fun openCityDetailsScreen(personId: Int)

    fun saveData(list: List<City>)

    fun loadData(): List<City>

    fun getDate(): String?
}
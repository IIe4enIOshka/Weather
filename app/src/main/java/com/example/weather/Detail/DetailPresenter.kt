package com.example.weather.Detail

import com.example.weather.BasePresenter
import com.example.weather.Entity.City
import com.example.weather.Service.WeatherService

class DetailPresenter(
    private val service: WeatherService,
    private val cityId: Int,
    private val citys: List<City>
) : BasePresenter<DetailView>() {

    override fun onViewAttached() {
        val city = service.getCity(cityId, citys)
        if (city != null) {
            view?.bindCity(city)
        } else {
            view?.closeScreen()
        }
    }
}

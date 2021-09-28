package com.example.newmyweaterproject.ui.main.model

import retrofit2.Callback

interface DetailsRepository {
    fun getWeatherDetailFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    )
}
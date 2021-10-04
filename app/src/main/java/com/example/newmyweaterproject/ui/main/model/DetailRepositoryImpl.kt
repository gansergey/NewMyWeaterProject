package com.example.newmyweaterproject.ui.main.model

import retrofit2.Callback


class DetailRepositoryImpl(private val remoteDataSource: RemoteDataSource) : DetailsRepository {

    override fun getWeatherDetailFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetail(lat, lon, callback)
    }
}
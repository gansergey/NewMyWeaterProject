package com.example.newmyweaterproject.ui.main.model


import com.example.newmyweaterproject.BuildConfig
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {

    private val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.weather.yandex.ru/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(WeatherApi::class.java)

    fun getWeatherDetail(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
        weatherApi.getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).enqueue(callback)
    }

}
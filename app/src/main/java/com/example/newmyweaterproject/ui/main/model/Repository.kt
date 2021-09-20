package com.example.newmyweaterproject.ui.main.model

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorageWorld(): List<Weather>
    fun getWeatherFromLocalStorageRus(): List<Weather>
}
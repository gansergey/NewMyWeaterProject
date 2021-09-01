package com.example.newmyweaterproject.ui.main.model

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather
}
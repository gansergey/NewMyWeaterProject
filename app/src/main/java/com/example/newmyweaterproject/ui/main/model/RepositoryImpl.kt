package com.example.newmyweaterproject.ui.main.model

class RepositoryImpl : Repository {

    override fun getWeatherFromServer(): Weather = Weather()

    override fun getWeatherFromLocalStorageWorld(): List<Weather> = getWorldCity()

    override fun getWeatherFromLocalStorageRus(): List<Weather> = getRussiaCity()
}
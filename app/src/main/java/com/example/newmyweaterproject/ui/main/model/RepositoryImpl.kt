package com.example.newmyweaterproject.ui.main.model

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCity()
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getRussiaCity()
    }
}
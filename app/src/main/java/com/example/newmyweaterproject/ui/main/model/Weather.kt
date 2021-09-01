package com.example.newmyweaterproject.ui.main.model

data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0
)

private fun getDefaultCity(): City {
    return City("Москва", 55.755826, 37.617299900000035)
}

package com.example.newmyweaterproject.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newmyweaterproject.ui.main.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DetailsRepository = DetailRepositoryImpl(RemoteDataSource())
    private val detailsLiveData = MutableLiveData<AppState>()

    val liveData: LiveData<AppState> = detailsLiveData

    fun getWeatherFromRemoteSource(weather: Weather) {
        detailsLiveData.value = AppState.Loading
        repository.getWeatherDetailFromServer(
            weather.city.lat,
            weather.city.lon,
            object : Callback<WeatherDTO> {

                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {

                    response.body()?.let {
                        detailsLiveData.postValue(checkResponse(it, weather.city))
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    detailsLiveData.postValue(AppState.Error(t))
                }
            })
    }

    private fun checkResponse(response: WeatherDTO, city: City): AppState {

        val factDTO = response.fact

        return if (
            factDTO?.condition != null
            && factDTO.temp != null
            && factDTO.feels_like != null
        ) {
            AppState.Success(
                listOf(
                    Weather(
                        city = city,
                        condition = factDTO.condition,
                        temperature = factDTO.temp,
                        feelsLike = factDTO.feels_like
                    )
                )
            )
        } else {
            AppState.Error(ParseException("Невозможно распартить Json", 0))
        }
    }
}
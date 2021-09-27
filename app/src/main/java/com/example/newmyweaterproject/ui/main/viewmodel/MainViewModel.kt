package com.example.newmyweaterproject.ui.main.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newmyweaterproject.ui.main.model.Repository
import com.example.newmyweaterproject.ui.main.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repository: Repository = RepositoryImpl()
    private var liveDataIsRusToObserve: MutableLiveData<Boolean> = MutableLiveData(true)

    val liveData: LiveData<AppState> = liveDataToObserve
    val liveDataIsRus: LiveData<Boolean> = liveDataIsRusToObserve

    //Вызывается при открытии экрана
    fun getWeatherFromLocalSource() = getDataFromLocalSource()

    //fun getWeatherFromRemoteSource() = getDataFromLocalSource()
    fun onLanguageChange() {
        liveDataIsRusToObserve.value = liveDataIsRusToObserve.value == false
    }

    fun onLanguageDefaultLoading(isWorld: Boolean){
        liveDataIsRusToObserve.value = isWorld == true //сохраченный if else. Надо почитать
    }

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        sleep(1000)
        //postValue Делается из другого потока
        liveDataToObserve.postValue(
            if (liveDataIsRusToObserve.value == true) {
                AppState.Success(repository.getWeatherFromLocalStorageRus())
            } else {
                AppState.Success(repository.getWeatherFromLocalStorageWorld())
            }
        )
    }
}
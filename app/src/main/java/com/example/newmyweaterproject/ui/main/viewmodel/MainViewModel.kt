package com.example.newmyweaterproject.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newmyweaterproject.ui.main.model.Repository
import com.example.newmyweaterproject.ui.main.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repository: Repository = RepositoryImpl()
    private var liveDataIsRusToObserve: MutableLiveData<Boolean> = MutableLiveData(true)

    val liveData: LiveData<AppState> = liveDataToObserve
    val liveDataIsRus: LiveData<Boolean> = liveDataIsRusToObserve

    //Вызывается при открытии экрана
    fun getWeatherFromLocalSource() = getDataFromLocalSource()

    //    fun getWeatherFromRemoteSource() = getDataFromLocalSource()
    fun onLanguageChange() {
        liveDataIsRusToObserve.value = liveDataIsRusToObserve.value == false
    }

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(3000)

            //postValue Делается из другого потока
            liveDataToObserve.postValue(
                if (liveDataIsRusToObserve.value == true) {
                    AppState.Success(repository.getWeatherFromLocalStorageRus())
                } else {
                    AppState.Success(repository.getWeatherFromLocalStorageWorld())
                }
            )
        }.start()
    }
}
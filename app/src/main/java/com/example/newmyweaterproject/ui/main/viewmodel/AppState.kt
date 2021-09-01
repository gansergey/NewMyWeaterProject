package com.example.newmyweaterproject.ui.main.viewmodel

import com.example.newmyweaterproject.ui.main.model.Weather

sealed class AppState {
    data class Success(val weather: Weather) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
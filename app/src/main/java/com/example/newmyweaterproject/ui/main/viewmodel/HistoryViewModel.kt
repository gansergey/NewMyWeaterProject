package com.example.newmyweaterproject.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.newmyweaterproject.ui.main.model.LocalRepositoryImpl
import com.example.newmyweaterproject.ui.main.model.database.HistoryEntity
import com.example.newmyweaterproject.ui.main.view.App

class HistoryViewModel : ViewModel() {
    private val historyRepository = LocalRepositoryImpl(App.getHistoryDao())
    fun getAllHistory(): List<HistoryEntity> = historyRepository.getAllHistory()
}
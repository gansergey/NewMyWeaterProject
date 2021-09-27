package com.example.newmyweaterproject.ui.main.model

import com.example.newmyweaterproject.ui.main.model.database.HistoryEntity

interface LocalRepository {
    fun getAllHistory(): List<HistoryEntity>
    fun saveEntity(weather: HistoryEntity)
}
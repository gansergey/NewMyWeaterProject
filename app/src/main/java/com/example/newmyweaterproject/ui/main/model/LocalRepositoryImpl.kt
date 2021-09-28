package com.example.newmyweaterproject.ui.main.model

import com.example.newmyweaterproject.ui.main.model.database.HistoryDao
import com.example.newmyweaterproject.ui.main.model.database.HistoryEntity

class LocalRepositoryImpl(
    private val dao: HistoryDao
) : LocalRepository {
    override fun getAllHistory(): List<HistoryEntity> = dao.all()

    override fun saveEntity(weather: HistoryEntity) {
        dao.insert(weather)
    }
}
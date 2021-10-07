package com.example.newmyweaterproject.ui.main.view

import android.app.Application
import android.app.Notification
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.room.Room
import com.example.newmyweaterproject.R
import com.example.newmyweaterproject.ui.main.model.database.HistoryDao
import com.example.newmyweaterproject.ui.main.model.database.HistoryDataBase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Fetching FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("Fetching FCM", token)
            makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {

        private var appInstance: App? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"

        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            HistoryDataBase::class.java,
                            DB_NAME
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return db!!.historyDao()
        }
    }
}


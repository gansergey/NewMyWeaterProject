package com.example.newmyweaterproject.ui.main.view

import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newmyweaterproject.R
import com.example.newmyweaterproject.databinding.MainActivityBinding
import com.example.newmyweaterproject.ui.main.model.MainBroadcastReceiver


class MainActivity : AppCompatActivity() {

    private val receiver = MainBroadcastReceiver()

    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        registerReceiver(receiver, IntentFilter(CONNECTIVITY_ACTION))


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}
package com.example.newmyweaterproject.ui.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newmyweaterproject.R
import com.example.newmyweaterproject.databinding.MainActivityBinding


class MainActivity : AppCompatActivity() {

    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}
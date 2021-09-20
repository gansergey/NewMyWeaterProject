package com.example.newmyweaterproject.ui.main.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.example.newmyweaterproject.R

class MainBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (!hasConnection(context)) {
            StringBuilder().apply {
                append("${context.resources.getString(R.string.warning_massage_internet)}\n")
                append(context.resources.getString(R.string.massage_internet))
                toString().also {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(
                context,
                context.resources.getString(R.string.massage_internet_connect),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun hasConnection(context: Context): Boolean {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        var state = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        if (state != null && state.isConnected) {
            return true
        }

        state = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

        if (state != null && state.isConnected) {
            return true
        }

        state = cm.activeNetworkInfo
        return state != null && state.isConnected
    }

}
package com.example.newmyweaterproject.ui.main.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.newmyweaterproject.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(
    private val lat: Double,
    private val lon: Double,
    private val listener: WeatherLoaderListener
) {

    private val WEATHER_EXTRA = "X-Yandex-API-Key"

    @RequiresApi(Build.VERSION_CODES.N)
    fun goToInternet() {

        Thread {
            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=-${lon}&lang=ru_RU")
            var urlConnection: HttpURLConnection? = null
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.apply {
                    requestMethod = "GET"
                    readTimeout = 10000
                    addRequestProperty(WEATHER_EXTRA, BuildConfig.WEATHER_API_KEY)
                }

                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val result = reader.lines().collect(Collectors.joining("\n"))

                //Парсим JSON
                val weatherDTO: WeatherDTO = Gson().fromJson(result, WeatherDTO::class.java)

                listener.onLoaded(weatherDTO)

            } catch (ex: Exception) {
                listener.onFailed(ex)
                Log.e("", "FAILED", ex)
            } finally {
                urlConnection?.disconnect()
            }
        }.start()
    }

    interface WeatherLoaderListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed(throwable: Exception)
    }
}

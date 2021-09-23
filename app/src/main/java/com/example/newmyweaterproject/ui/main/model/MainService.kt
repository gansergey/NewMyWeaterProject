package com.example.newmyweaterproject.ui.main.model

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.newmyweaterproject.BuildConfig
import com.example.newmyweaterproject.ui.main.view.DetailsFragment.Companion.WEATHER_EXTRA
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


const val YANDEX_API = "X-Yandex-API-Key"
const val MAIN_SERVICE_STRING_EXTRA = "MainServiceExtra"
const val LAT_EXTRA = "Latitude"
const val LON_EXTRA = "Longitude"
const val DETAILS_INTENT_FILTER = "DETAILS_INTENT_FILTER"
const val FACT_WEATHER_EXTRA = "FACT_WEATHER_EXTRA"
const val RESULT_EXTRA = "RESULT_EXTRA"
const val SUCCESS_RESULT = "SUCCESS_RESULT"
const val ERROR_EMPTY_DATA_RESULT = "ERROR_EMPTY_DATA_RESULT"


class MainService(name: String = "MainService") : IntentService(name) {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        Log.d("MainService", "OnHandlerIntent ${intent?.getStringExtra(MAIN_SERVICE_STRING_EXTRA)}")
        intent?.let {
            val lat = intent.getDoubleExtra(LAT_EXTRA, 0.0)
            val lon = intent.getDoubleExtra(LON_EXTRA, 0.0)

            if (lat == 0.0 && lon == 0.0) {
                onEmptyData()
            } else {
                loadWeather(lat, lon)
            }
        } ?: onEmptyIntent()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadWeather(lat: Double, lon: Double) {
        var urlConnection: HttpURLConnection? = null

        val uri = try {
            URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}&lang=ru_RU")
        } catch (ex: MalformedURLException) {
            onMalformedURL()
            return
        }
        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.apply {
                requestMethod = "GET"
                readTimeout = 10000
                addRequestProperty(YANDEX_API, BuildConfig.WEATHER_API_KEY)
            }

            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val result = reader.lines().collect(Collectors.joining("\n"))

            //Парсим JSON
            val weatherDTO: WeatherDTO = Gson().fromJson(result, WeatherDTO::class.java)

            onResponse(weatherDTO)

        } catch (ex: Exception) {
            onErrorResponse(ex.message ?: "Unknown Error")
            Log.e("", "FAILED", ex)
        } finally {
            urlConnection?.disconnect()
        }
    }

    private fun onMalformedURL() {
        Toast.makeText(this,"Unable to get the link",Toast.LENGTH_SHORT).show();
    }

    private fun onResponse(weatherDTO: WeatherDTO) {
        weatherDTO.fact?.let {
            onSuccessResponse(it)
        } ?: onEmptyResponse()
    }

    private fun onSuccessResponse(it: WeatherDTO.FactDTO) {
        //После получаения данных отправляем данные в активити
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(
                Intent(DETAILS_INTENT_FILTER)
                    .putExtra(RESULT_EXTRA, SUCCESS_RESULT)
                    .putExtra(FACT_WEATHER_EXTRA, it)
            )
    }

    private fun onErrorResponse(s: String) {
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(
                Intent(DETAILS_INTENT_FILTER)
                    .putExtra(RESULT_EXTRA, ERROR_EMPTY_DATA_RESULT)
            )
    }

    private fun onEmptyResponse() {
        TODO("Not yet implemented")
    }

    private fun onEmptyIntent() {
        TODO("Not yet implemented")
    }

    private fun onEmptyData() {
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(
                Intent(DETAILS_INTENT_FILTER)
                    .putExtra(RESULT_EXTRA, ERROR_EMPTY_DATA_RESULT)
            )
    }

}
package com.example.newmyweaterproject.ui.main.view

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.newmyweaterproject.R
import com.example.newmyweaterproject.databinding.FragmentDetailBinding
import com.example.newmyweaterproject.ui.main.model.*

class DetailsFragment : Fragment() {

    companion object {
        const val WEATHER_EXTRA = "WEATHER_EXTRA"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val localResultBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.getStringExtra(RESULT_EXTRA)) {
                SUCCESS_RESULT -> {
                    intent.getParcelableExtra<WeatherDTO.FactDTO>(FACT_WEATHER_EXTRA)?.let {
                        displayWeather(it)
                    }
                }
                else -> {
                    binding.mainDetail.showSnackBar("Error","Try again",{ view ->
                    })
                }

            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(localResultBroadcastReceiver, IntentFilter(DETAILS_INTENT_FILTER))
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(localResultBroadcastReceiver)
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Изменяем для binding
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        _binding = FragmentDetailBinding.bind(view)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(WEATHER_EXTRA)?.let {
            with(binding) {
                it.city.also { city ->
                    tvCity.text = city.name
                    tvLatLon.text = "${getString(R.string.lat)} ${city.lat}, " +
                            "${getString(R.string.lon)} ${city.lon}"

                    getWeather(city.lat, city.lon)

//                    WeatherLoader(
//                        city.lat,
//                        city.lon,
//                        object : WeatherLoader.WeatherLoaderListener {
//                            override fun onLoaded(weatherDTO: WeatherDTO) {
//                                requireActivity().runOnUiThread {
//                                    displayWeather(weatherDTO)
//                                }
//                            }
//
//                            override fun onFailed(throwable: Exception) {
//                                requireActivity().runOnUiThread {
//                                    Toast.makeText(
//                                        requireContext(),
//                                        throwable.localizedMessage,
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                            }
//
//                        }).goToInternet()
                }
            }

        }
    }

    private fun getWeather(lat: Double, lon: Double) {
        //binding.loadingLayout.showOrHide(false)
        requireActivity().startService(Intent(requireContext(), MainService::class.java).apply {
            putExtra(LAT_EXTRA, lat)
            putExtra(LON_EXTRA, lon)
        })
    }

    private fun displayWeather(weather: WeatherDTO.FactDTO) {
        with(binding) {
            weather.also {
                //loadingLayout.showOrHide(true)
                tvTemperature.text = weather.temp.toString()
                tvFeelsLike.text = weather.feels_like.toString()
            }
        }
    }

    //для binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
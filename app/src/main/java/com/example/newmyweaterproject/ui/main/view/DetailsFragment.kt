package com.example.newmyweaterproject.ui.main.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.newmyweaterproject.R
import com.example.newmyweaterproject.databinding.FragmentDetailBinding
import com.example.newmyweaterproject.ui.main.model.Weather
import com.example.newmyweaterproject.ui.main.model.WeatherDTO
import com.example.newmyweaterproject.ui.main.model.WeatherLoader

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
                    WeatherLoader(
                        city.lat,
                        city.lon,
                        object : WeatherLoader.WeatherLoaderListener {
                            override fun onLoaded(weatherDTO: WeatherDTO) {
                                requireActivity().runOnUiThread {
                                    displayWeather(weatherDTO)
                                }
                            }
                            override fun onFailed(throwable: Exception) {
                                requireActivity().runOnUiThread {
                                    Toast.makeText(
                                        requireContext(),
                                        throwable.localizedMessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        }).goToInternet()
                }
            }

        }
    }

    private fun displayWeather(weather: WeatherDTO) {
        with(binding) {
            weather.also {
                tvTemperature.text = weather.fact?.temp.toString()
                tvFeelsLike.text = weather.fact?.feels_like.toString()
            }
        }
    }

    //для binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
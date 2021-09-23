package com.example.newmyweaterproject.ui.main.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.newmyweaterproject.R
import com.example.newmyweaterproject.databinding.FragmentDetailBinding
import com.example.newmyweaterproject.ui.main.model.Weather
import com.example.newmyweaterproject.ui.main.model.WeatherDTO
import com.example.newmyweaterproject.ui.main.viewmodel.AppState
import com.example.newmyweaterproject.ui.main.viewmodel.DetailViewModel

class DetailsFragment : Fragment() {

    companion object {
        const val WEATHER_EXTRA = "WEATHER_EXTRA"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
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
        val weather = arguments?.getParcelable(WEATHER_EXTRA) ?: Weather()

        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            renderData(state)
        }

        viewModel.getWeatherFromRemoteSource(weather)
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(state: AppState) {

        when (state) {
            is AppState.Loading -> binding.loadingLayout.showOrHide(true)
            is AppState.Success -> {
                val weather = state.weather.first()
                with(binding) {
                    loadingLayout.showOrHide(false)

                    val condition = weather.condition.toString()

                    tvTemperature.text = weather.temperature.toString()
                    tvFeelsLike.text = weather.feelsLike.toString()
                    tvCondition.text = condition

                    tvCity.text = weather.city.name
                    tvLatLon.text = "${getString(R.string.lat)} ${weather.city.lat}, " +
                            "${getString(R.string.lon)} ${weather.city.lon}"

                    getImageWeather(condition)
                }

            }
            is AppState.Error -> {

                with(binding) {
                    loadingLayout.showOrHide(false)
                    mainDetail.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        { viewModel.getWeatherFromRemoteSource(Weather()) }
                    )
                }
            }
        }
    }

    //для binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getImageWeather(condition: String){

        when (condition) {
            //ясно
            "clear" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/890/890347.png")
            //малооблачно
            "partly-cloudy" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3208/3208752.png")
            // облачно с прояснениями
            "cloudy" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3208/3208752.png")
            //пасмурно
            "overcast" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/2676/2676023.png")
            //морось
            "drizzle" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3520/3520675.png")
            //небольшой дождь
            "light-rain" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3520/3520675.png")
            //дождь
            "rain" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3520/3520675.png")
            //умеренно сильный дождь
            "moderate-rain" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3520/3520675.png")
            //сильный дождь
            "heavy-rain" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3520/3520675.png")
            //длительный сильный дождь
            "continuous-heavy-rain" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3520/3520675.png")
            // ливень
            "showers" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3520/3520675.png")
            // дождь со снегом
            "wet-snow" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/1164/1164956.png")
            // небольшой снег
            "light-snow" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/414/414968.png")
            //снег
            "snow" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/414/414968.png")
            //снегопад
            "snow-showers" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/414/414968.png")
            //град
            "hail" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3496/3496239.png")
            //гроза
            "thunderstorm" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3436/3436159.png")
            //дождь с грозой
            "thunderstorm-with-rain" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3436/3436159.png")
            //гроза с градом
            "thunderstorm-with-hail" -> loadImageWeather("https://cdn-icons-png.flaticon.com/512/3436/3436159.png")
            else -> {
                loadImageWeather("https://cdn-icons-png.flaticon.com/512/821/821924.png")
            }
        }

    }

    private fun loadImageWeather(link: String){
        binding.imageView.load(link)
    }


}
package com.example.newmyweaterproject.ui.main.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newmyweaterproject.R
import com.example.newmyweaterproject.databinding.MainFragmentBinding
import com.example.newmyweaterproject.ui.main.viewmodel.AppState
import com.example.newmyweaterproject.ui.main.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private val IS_WORLD_KEY = "LIST_OF_TOWNS_KEY"

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter: MainAdapter by lazy { MainAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Изменяем для binding
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        _binding = MainFragmentBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.listener = MainAdapter.OnItemViewClickListener { weather ->
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.WEATHER_EXTRA, weather)
                    }))
                    .addToBackStack("")
                    .commit()
            }
        }

        with(binding) {
            recyclerView.adapter = adapter
            mainFragmentFab.setOnClickListener {
                viewModel.onLanguageChange()
            }
        }

        with(viewModel) {
            //В этом методе мы подписываемся на изменения. Будем класть наш текст в LiveData
            liveData.observe(viewLifecycleOwner, { state: AppState ->
                renderData(state)
            })

            //Подписываемся на изменения переключения стран
            liveDataIsRus.observe(viewLifecycleOwner, { isRus ->
                with(binding) {
                    if (isRus) {
                        mainFragmentFab.setImageResource(R.drawable.ic_russia)
                        saveListOfTowns(false)
                    } else {
                        mainFragmentFab.setImageResource(R.drawable.ic_world)
                        saveListOfTowns(true)
                    }
                }
                getWeatherFromLocalSource()
            })
        }
        showListOfTowns()
    }

    //Передаём в метод значение False или True. Если True будет выводиться список World
    private fun saveListOfTowns(isDataSetWorld: Boolean) {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_WORLD_KEY, isDataSetWorld)
                apply()
            }
        }
    }

    //Получаем значание из настроек выбранного списка городов в предыдущем использовании
    private fun showListOfTowns() {
        activity?.let {
            //читаем настройки из файла если настроек нет возвращаем FALSE, или значение равно False
            if (it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_WORLD_KEY, false)) {
                viewModel.onLanguageDefaultLoading(false)
            //Иначе запускаем метод в соответствии с нашей настройкой
            } else {
                viewModel.onLanguageDefaultLoading(true)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Loading -> binding.loadingLayout.showOrHide(true)
            is AppState.Success -> {
                binding.loadingLayout.showOrHide(false)
                adapter.weatherData = state.weather
            }
            is AppState.Error -> {
                binding.loadingLayout.showOrHide(false)
                binding.mainFragmentFab.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeatherFromLocalSource() }
                )
            }
        }
    }

    //для binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
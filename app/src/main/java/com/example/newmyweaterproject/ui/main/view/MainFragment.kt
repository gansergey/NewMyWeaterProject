package com.example.newmyweaterproject.ui.main.view

import android.annotation.SuppressLint
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
                    .add(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.WEATHER_EXTRA, weather)
                    }))
                    .addToBackStack("")
                    .commit()
            }
        }

        binding.recyclerView.adapter = adapter
        binding.mainFragmentFab.setOnClickListener {
            viewModel.onLanguageChange()
        }


        //В этом методе мы подписываемся на изменения. Будем класть наш текст в LiveData
        viewModel.liveData.observe(viewLifecycleOwner, { state: AppState ->
            renderData(state)
        })

        //Подписываемся на изменения переключения стран
        viewModel.liveDataIsRus.observe(viewLifecycleOwner, { isRus ->
            if (isRus) {
                binding.mainFragmentFab.setImageResource(R.drawable.ic_russia)
            } else {
                binding.mainFragmentFab.setImageResource(R.drawable.ic_world)
            }
            viewModel.getWeatherFromLocalSource()
        })
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
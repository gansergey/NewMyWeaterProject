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
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MainAdapter

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

        adapter = MainAdapter()

        adapter.listener =
            MainAdapter.OnItemViewClickListener { weather ->
                val manager = activity?.supportFragmentManager
                if (manager != null) {
                    val bundle = Bundle()
                    bundle.putParcelable(DetailsFragment.WEATHER_EXTRA, weather)
                    manager.beginTransaction()
                        .replace(R.id.container, DetailsFragment.newInstance(bundle))
                        .addToBackStack("")
                        .commit()
                }
            }

        binding.recyclerView.adapter = adapter
        binding.mainFragmentFab.setOnClickListener {
            viewModel.onLanguageChange()
        }


        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

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
            is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.weatherData = state.weather
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(
                        binding.mainFragmentFab,
                        getString(R.string.error),
                        Snackbar.LENGTH_INDEFINITE
                    )
                    .setAction(getString(R.string.reload)) {
                        viewModel.getWeatherFromLocalSource()
                    }.show()
            }
        }

    }

    //для binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
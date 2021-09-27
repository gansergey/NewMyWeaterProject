package com.example.newmyweaterproject.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newmyweaterproject.R
import com.example.newmyweaterproject.databinding.FragmentHistoryBinding
import com.example.newmyweaterproject.ui.main.viewmodel.HistoryAdapter
import com.example.newmyweaterproject.ui.main.viewmodel.HistoryViewModel


class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Изменяем для binding
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        _binding = FragmentHistoryBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewHistory.adapter = adapter
        adapter.setData(viewModel.getAllHistory())
    }

    //для binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


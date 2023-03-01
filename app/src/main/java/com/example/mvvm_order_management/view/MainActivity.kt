package com.example.mvvm_order_management.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_order_management.databinding.ActivityMainBinding
import com.example.mvvm_order_management.model.LoadingState
import com.example.mvvm_order_management.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter = OrderAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initializeUi()
        initializeObservers()
        viewModel.onViewReady()
    }

    private fun initializeUi() {
        binding.apply {
            ordersRv.adapter = adapter
            ordersRv.layoutManager = LinearLayoutManager(this@MainActivity)
            searchEt.doOnTextChanged { text, _, _, _ ->
                viewModel.onSearchQuery(text.toString())
            }
        }
    }

    private fun initializeObservers() {
        viewModel.loadingStateLiveData.observe(this) {
            onLoadingStateChanged(it)
        }
        viewModel.ordersLiveData.observe(this) {
            adapter.updateOrders(it)
        }
    }

    private fun onLoadingStateChanged(loadingState: LoadingState?) {
        binding.apply {
            searchEt.visibility = visibilityHandler(loadingState, LoadingState.LOADED)
            ordersRv.visibility = visibilityHandler(loadingState, LoadingState.LOADED)
            errorTv.visibility = visibilityHandler(loadingState, LoadingState.ERROR)
            loadingPb.visibility = visibilityHandler(loadingState, LoadingState.LOADING)
        }
    }

    private fun visibilityHandler(state: LoadingState?, loadingState: LoadingState?) =
        if (state == loadingState) View.VISIBLE else View.GONE
}
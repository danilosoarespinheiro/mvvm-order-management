package com.example.mvvm_order_management.viewmodel

import androidx.lifecycle.*
import com.example.mvvm_order_management.model.LoadingState
import com.example.mvvm_order_management.model.Order
import com.example.mvvm_order_management.model.OrderDataGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val ordersLiveData = MediatorLiveData<List<Order>>()
    val loadingStateLiveData = MutableLiveData<LoadingState>()
    private val _queryLiveData = MutableLiveData<String>()
    private val _allOrdersLiveData = MutableLiveData<List<Order>>()
    private val _searchOrdersLiveData: LiveData<List<Order>>
    private var searchJob: Job? = null
    private val debouncePeriod = 500L

    init {
        _searchOrdersLiveData = Transformations.switchMap(_queryLiveData) {
            fetchOrdersByQuery(it)
        }

        ordersLiveData.addSource(_allOrdersLiveData) {
            ordersLiveData.value = it
        }

        ordersLiveData.addSource(_searchOrdersLiveData) {
            ordersLiveData.value = it
        }
    }

    private fun fetchOrdersByQuery(query: String): LiveData<List<Order>> {
        val liveData = MutableLiveData<List<Order>>()
        loadingStateLiveData.value = LoadingState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val orders = OrderDataGenerator.searchOrders(query)
                liveData.postValue(orders)
                loadingStateLiveData.postValue(LoadingState.LOADED)
            } catch (e: Exception) {
                loadingStateLiveData.postValue(LoadingState.ERROR)
            }
        }
        return liveData
    }

    fun onViewReady() {
        if (_allOrdersLiveData.value.isNullOrEmpty()) fetchAllOrders()
    }

    private fun fetchAllOrders() {
        loadingStateLiveData.value = LoadingState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val orders = OrderDataGenerator.getAllOrders()
                _allOrdersLiveData.postValue(orders)
                loadingStateLiveData.postValue(LoadingState.LOADED)
            } catch (e: Exception) {
                loadingStateLiveData.postValue(LoadingState.ERROR)
            }
        }
    }

    fun onSearchQuery(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(debouncePeriod)
            if (query.isEmpty()) fetchAllOrders()
            else _queryLiveData.postValue(query)
        }
    }
}
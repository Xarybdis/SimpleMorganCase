package com.example.simplemorgancase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.simplemorgancase.network.Api
import com.example.simplemorgancase.network.Resource
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val api: Api) : ViewModel() {

    fun fetchAlbums() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = api.fetchAlbums()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "An error occurred!"))
        }
    }
}
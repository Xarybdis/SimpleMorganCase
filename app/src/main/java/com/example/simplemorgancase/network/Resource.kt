package com.example.simplemorgancase.network

import com.example.simplemorgancase.network.model.Status

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = Status.SUCCESS, data = data, message = null)

        fun <T> loading(): Resource<T> = Resource(status = Status.LOADING, data = null, message = null)

        fun <T> error(message: String?): Resource<T> = Resource(status = Status.ERROR, null, message = message)
    }
}
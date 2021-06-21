package com.example.simplemorgancase.di

import com.example.simplemorgancase.network.Api
import com.example.simplemorgancase.viewmodel.MainViewModel
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

/**
 *Handle retrofit instance creation with DI
 */
val networkModule = module(override = true) {
    single(named("BASE_URL")) { BASE_URL }

    /**
     * Create Interceptor.
     */
    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            client.addInterceptor(get<HttpLoggingInterceptor>())
                .addNetworkInterceptor(StethoInterceptor())
        }

        client.build()
    }

    /**
     * Build retrofit.
     */
    single {
        Retrofit.Builder()
            .baseUrl(get<String>(named("BASE_URL")))
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    /**
     * Creates and provides retrofit instance
     */
    single {
        get<Retrofit>().create(Api::class.java)
    }
}

/**
 * Provides ViewModel
 */
val viewModelModule = module(override = true) {
    single { MainViewModel(get()) }
}
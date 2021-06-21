package com.example.simplemorgancase.network

import com.example.simplemorgancase.network.model.AlbumResponse
import retrofit2.http.GET

interface Api {
    @GET("albums")
    suspend fun fetchAlbums(): List<AlbumResponse>
}
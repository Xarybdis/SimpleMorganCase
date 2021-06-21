package com.example.simplemorgancase.network

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import com.example.simplemorgancase.network.model.AlbumResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPrefs(private val activity: Activity) {

    companion object {
        private const val KEY_ALBUM_CACHE = "album_list"
        private const val KEY_FIRST_START = "first_start"
    }

    private val gson = Gson()
    private val sharedPrefs = activity.getPreferences(MODE_PRIVATE)

    fun setFirstStart(isFirstTimeStarted: Boolean) {
        sharedPrefs.edit().putBoolean(KEY_FIRST_START, isFirstTimeStarted).apply()
    }

    fun setAlbumsList(list: MutableList<AlbumResponse>) {
        val list = gson.toJson(list)
        sharedPrefs.edit().putString(KEY_ALBUM_CACHE, list).apply()
    }

    fun isFirstTime(): Boolean = sharedPrefs.getBoolean(KEY_FIRST_START, true)

    fun getAlbumsList(): MutableList<AlbumResponse> {
        val json = sharedPrefs.getString(KEY_ALBUM_CACHE, null)
        val type: Type = object : TypeToken<List<AlbumResponse>?>() {}.type
        return gson.fromJson(json, type)
    }
}
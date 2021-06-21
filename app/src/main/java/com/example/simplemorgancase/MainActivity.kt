package com.example.simplemorgancase

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.simplemorgancase.databinding.ActivityMainBinding
import com.example.simplemorgancase.network.SharedPrefs
import com.example.simplemorgancase.network.model.AlbumResponse
import com.example.simplemorgancase.network.model.Status
import com.example.simplemorgancase.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by inject()
    private lateinit var albumsRwAdapter: AlbumsRwAdapter
    private var albumsList = mutableListOf<AlbumResponse>()
    private lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Darkmode off

        sharedPrefs = SharedPrefs(this)
        setRecyclerView()
        viewClicks()
        filterList()
    }

    override fun onResume() {
        super.onResume()
        cacheControl()
    }

    private fun viewClicks() {
        binding.refreshButton.setOnClickListener {
            fetchAlbums()
        }
    }

    private fun setRecyclerView() {
        albumsRwAdapter = AlbumsRwAdapter(this)
        binding.recyclerAlbums.setHasFixedSize(true)
        binding.recyclerAlbums.adapter = albumsRwAdapter
    }

    private fun cacheControl() {
        if (sharedPrefs.isFirstTime()) {
            fetchAlbums()
        } else {
            if (sharedPrefs.getAlbumsList().isNullOrEmpty()) {
                fetchAlbums()
            } else {
                albumsRwAdapter.updateList(sharedPrefs.getAlbumsList())
                albumsList = sharedPrefs.getAlbumsList()

            }
        }
    }

    private fun fetchAlbums() {
        viewModel.fetchAlbums().observe(this, Observer {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.loadingView.root.isVisible = false
                        it.data?.let { it1 ->
                            albumsRwAdapter.updateList(it1)
                            albumsList = it1.toMutableList()
                            sharedPrefs.setAlbumsList(albumsList)
                            sharedPrefs.setFirstStart(false)
                        }
                    }
                    Status.ERROR -> {
                        binding.loadingView.root.isVisible = false
                        Toast.makeText(this, getString(R.string.error_message_text), Toast.LENGTH_SHORT).show()
                        Timber.e(it.message)
                    }
                    Status.LOADING -> {
                        binding.loadingView.root.isVisible = true
                    }
                }
            }
        })
    }

    private fun filterList() {
        binding.titSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(input: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(input: Editable?) {
                albumsRwAdapter.updateList(albumsList.filter {
                    it.userId.toString().contains(input.toString()) || it.id.toString().contains(input.toString()) || it.title?.contains(
                        input.toString().toLowerCase()
                    )!!
                })
            }
        })
    }
}
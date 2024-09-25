package com.example.storyapps.view.maps

import androidx.lifecycle.ViewModel
import com.example.storyapps.data.MapsRepository

class MapsViewModel(private val mapsRepository: MapsRepository) : ViewModel() {

    val maps = mapsRepository.getStoriesLocation()

    companion object {
        private const val TAG = "MapsViewModel"
    }
}
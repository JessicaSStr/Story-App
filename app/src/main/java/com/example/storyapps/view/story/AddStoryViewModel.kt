package com.example.storyapps.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapps.data.StoryRepository
import com.example.storyapps.remote.response.UploadResponse
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _responseMsg = MutableLiveData<String>()
    val responseMsg: LiveData<String> = _responseMsg

    private val _responseCode = MutableLiveData<Int>()
    val responseCode: LiveData<Int> = _responseCode

    private val _responseUp = MutableLiveData<UploadResponse>()
    val responseUp: LiveData<UploadResponse> = _responseUp

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun uploadStory(file: File, description: String) =
        storyRepository.uploadStory(file, description)

    companion object {
        private const val TAG = "AddStoryViewModel"
    }
}
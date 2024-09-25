package com.example.storyapps.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapps.data.network.retrofit.ApiService
import com.example.storyapps.data.pref.UserPreference
import com.example.storyapps.remote.response.ListStoryItem
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.util.concurrent.TimeoutException

class MapsRepository(private val apiService: ApiService, private val preference: UserPreference) {

    fun getStoriesLocation(): LiveData<Resource<List<ListStoryItem>>> = liveData {
        emit(Resource.Loading)
        try {
            val location = 1
            val token = preference.getSession().first()
            val bearerToken = "Bearer $token"
            val response =
                apiService.getStories(token = bearerToken, size = 50, location = location)
            if (response.isSuccessful) {
                val body = response.body()
                val listStoryItem = body?.listStory
                if (listStoryItem?.isEmpty() == true) {
                    emit(Resource.Success(emptyList()))
                } else {
                    emit(Resource.Success(listStoryItem!!))
                }
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Periksa Internet"))
        } catch (e: TimeoutException) {
            emit(Resource.Error("Coba Lagi"))
        } catch (e: Exception) {
            emit(Resource.Error("Terjadi Kesalahan"))
        }
    }

    companion object {
        @Volatile
        private var instance: MapsRepository? = null
        fun getInstance(
            apiService: ApiService,
            preference: UserPreference
        ): MapsRepository =
            instance ?: synchronized(this) {
                instance ?: MapsRepository(apiService, preference)
            }.also { instance = it }
    }
}
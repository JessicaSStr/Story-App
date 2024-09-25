package com.example.storyapps.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapps.data.network.retrofit.ApiService
import com.example.storyapps.data.pref.UserPreference
import com.example.storyapps.database.StoryDatabase
import com.example.storyapps.remote.response.ListStoryItem
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeoutException

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService, private val preference: UserPreference) {

    suspend fun getStory(): LiveData<PagingData<ListStoryItem>> {
        val token = preference.getSession().first()
        val bearerToken = "Bearer $token"
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, bearerToken),
            pagingSourceFactory = {
                storyDatabase.storyDao().getStories()
//                StoryPagingSource(apiService, bearerToken)
            }
        ).liveData
    }

    fun uploadStory(file: File, description: String): LiveData<Resource<Unit>> = liveData {
        emit(Resource.Loading)
        try {
            val token = preference.getSession().first()
            val bearerToken = "Bearer $token"
            val requestimage = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("photo", file.name, requestimage)
            val requestdesc = description.toRequestBody("text/plain".toMediaType())
            val response = apiService.uploadImage(imagePart, requestdesc, bearerToken)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
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
        private var instance: StoryRepository? = null
        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService,
            preference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyDatabase,apiService, preference)
            }.also { instance = it }
    }
}
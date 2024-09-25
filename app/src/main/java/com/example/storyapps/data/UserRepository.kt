package com.example.storyapps.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapps.data.network.request.LoginRequest
import com.example.storyapps.data.network.request.RegisterRequest
import com.example.storyapps.data.network.response.ApiResponse
import com.example.storyapps.data.network.response.LoginResponse
import com.example.storyapps.data.network.retrofit.ApiService
import com.example.storyapps.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun register(registerRequest: RegisterRequest): LiveData<Resource<ApiResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.register(registerRequest)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    Log.d("UserRepository", "register : $it")
                    emit(Resource.Success(it))
                }
            } else {
                response.errorBody()?.let {
                    val error = JSONObject(it.string())
                    emit(Resource.Error(error.getString("message")))
                }
            }
        } catch (e: IOException) {
            emit(Resource.Error("Periksa koneksi internet"))
        } catch (e: SocketTimeoutException) {
            emit(Resource.Error("Waktu request habis, coba lagi"))
        } catch (e: retrofit2.HttpException) {
            emit(Resource.Error(e.message()))
        }

    }

    fun login(loginRequest: LoginRequest): LiveData<Resource<LoginResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.login(loginRequest)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    val token = it.loginResult?.token.orEmpty()
                    // save token and set session
                    userPreference.saveSession(token)
                    userPreference.login()
                    emit(Resource.Success(it))
                }
            } else {
                response.errorBody()?.let {
                    val error = JSONObject(it.string())
                }
            }
        } catch (e: IOException) {
            emit(Resource.Error("Periksa koneksi internet"))
        } catch (e: SocketTimeoutException) {
            emit(Resource.Error("Waktu request habis, coba lagi"))
        } catch (e: retrofit2.HttpException) {
            emit(Resource.Error(e.message()))
        }

    }

    fun getSession(): Flow<Boolean> {
        return userPreference.isLogin()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
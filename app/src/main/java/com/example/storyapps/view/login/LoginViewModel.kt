package com.example.storyapps.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapps.data.UserRepository
import com.example.storyapps.data.network.request.LoginRequest
import com.example.storyapps.data.network.request.RegisterRequest

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun register(registerRequest: RegisterRequest) = repository.register(registerRequest)
    fun login(loginRequest: LoginRequest) = repository.login(loginRequest)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    companion object {
        private const val TAG = "LoginViewModel"
    }
}
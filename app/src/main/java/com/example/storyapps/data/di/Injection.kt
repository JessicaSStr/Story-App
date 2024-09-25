package com.example.storyapps.data.di

import android.content.Context
import com.example.storyapps.data.MapsRepository
import com.example.storyapps.data.StoryRepository
import com.example.storyapps.data.UserRepository
import com.example.storyapps.data.network.retrofit.ApiConfig
import com.example.storyapps.data.pref.UserPreference
import com.example.storyapps.data.pref.dataStore
import com.example.storyapps.database.StoryDatabase

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val preference = UserPreference.getInstance(context.dataStore)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(database, apiService, preference)
    }

    fun provideMapsRepository(context: Context): MapsRepository {
        val apiService = ApiConfig.getApiService()
        val preference = UserPreference.getInstance(context.dataStore)
        return MapsRepository.getInstance(apiService, preference)
    }
}
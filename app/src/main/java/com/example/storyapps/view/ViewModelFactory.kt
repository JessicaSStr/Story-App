package com.example.storyapps.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.data.MapsRepository
import com.example.storyapps.data.StoryRepository
import com.example.storyapps.data.UserRepository
import com.example.storyapps.data.di.Injection
import com.example.storyapps.view.login.LoginViewModel
import com.example.storyapps.view.main.MainViewModel
import com.example.storyapps.view.maps.MapsViewModel
import com.example.storyapps.view.story.AddStoryViewModel

class ViewModelFactory(
    private val repository: UserRepository,
    private val storyRepository: StoryRepository,
    private val mapsRepository: MapsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository, repository) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(mapsRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideRepository(context),
                        Injection.provideStoryRepository(context),
                        Injection.provideMapsRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
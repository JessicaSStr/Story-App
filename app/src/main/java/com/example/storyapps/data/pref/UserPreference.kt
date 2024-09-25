package com.example.storyapps.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun isLogin(): Flow<Boolean> { return dataStore.data.map {
            it[IS_LOGIN_KEY] ?: false
        }
    }
    suspend fun saveSession(token: String) { dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }
    fun getSession(): Flow<String> { return dataStore.data.map { preferences ->
                preferences[TOKEN_KEY] ?: "no token"

        }
    }
    suspend fun login() { dataStore.edit { preferences ->
            preferences[IS_LOGIN_KEY] = true
        }
    }
    suspend fun logout() { dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
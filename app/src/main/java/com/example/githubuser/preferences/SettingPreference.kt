package com.example.githubuser.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val THEME_KEY = booleanPreferencesKey("theme_setting")
    private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    private val USERNAME = stringPreferencesKey("user_name")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { pref ->
            pref[THEME_KEY] ?: false
        }
    }

    fun isLoggedIn(): Flow<Boolean> {
        return dataStore.data.map { pref ->
            pref[IS_LOGGED_IN_KEY] ?: false
        }
    }

    fun getUsername(): Flow<String?> {
        return dataStore.data.map { pref ->
            pref[USERNAME]
        }
    }

    suspend fun saveThemeSetting (isDarkModeActivated: Boolean) {
        dataStore.edit {pref ->
            pref[THEME_KEY] = isDarkModeActivated
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
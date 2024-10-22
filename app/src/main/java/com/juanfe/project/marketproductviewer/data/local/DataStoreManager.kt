package com.juanfe.project.marketproductviewer.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        val SEARCH_HISTORY_KEY = stringPreferencesKey("search_history")
    }

    val allHistory: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[SEARCH_HISTORY_KEY] ?: ""
        }

    suspend fun saveSearch(query: String) {
        dataStore.edit { preferences ->
            preferences[SEARCH_HISTORY_KEY] = query
        }
    }
}


package com.example.ecommerce_app.feature_shopping.presentation.shopping.profile.language

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.data.data_store.PreferencesKeys
import com.example.ecommerce_app.core.domain.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
): ViewModel() {



    fun getCurrentLanguage() = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.currentLanguage] ?: Constant.ENGLISH
        }


    suspend fun changeToThisLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.currentLanguage] = language
        }
    }
}

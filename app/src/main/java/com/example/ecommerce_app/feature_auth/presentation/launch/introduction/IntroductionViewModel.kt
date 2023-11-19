package com.example.ecommerce_app.feature_auth.presentation.launch.introduction

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.data.data_store.PreferencesKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
): ViewModel() {
    suspend fun saveStartButtonClick() {
        dataStore.edit {
            it[PreferencesKeys.isStartBtnClicked] = true
        }
    }
}

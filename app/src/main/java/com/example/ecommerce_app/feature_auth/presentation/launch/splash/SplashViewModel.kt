package com.example.ecommerce_app.feature_auth.presentation.launch.splash

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.example.ecommerce_app.core.data.data_store.PreferencesKeys
import com.example.ecommerce_app.feature_auth.domain.util.StartingPreferences
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map

import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val auth: FirebaseAuth,
    private val context: Application
): ViewModel() {


    fun checkStartingPreferences() = dataStore.data
        .map { preferences ->
            StartingPreferences(
                isStartBtnClicked = preferences[PreferencesKeys.isStartBtnClicked] ?: false,
                isUserLogin = auth.currentUser?.uid != null
            )
        }


}
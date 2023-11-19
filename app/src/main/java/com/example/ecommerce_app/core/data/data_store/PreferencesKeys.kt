package com.example.ecommerce_app.core.data.data_store

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.ecommerce_app.core.domain.util.Constant

object PreferencesKeys {
    val isStartBtnClicked = booleanPreferencesKey(Constant.IS_START_BUTTON_CLICKED_DATA_STORE_KEY)

    val currentLanguage = stringPreferencesKey(Constant.CURRENT_LANGUAGE_DATA_STORE_KEY)
}
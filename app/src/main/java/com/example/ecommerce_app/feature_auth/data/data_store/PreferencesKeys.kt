package com.example.ecommerce_app.feature_auth.data.data_store

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.example.ecommerce_app.core.domain.util.Constant

object PreferencesKeys {
    val isStartBtnClicked = booleanPreferencesKey(Constant.IS_START_BUTTON_CLICKED_DATA_STORE_KEY)
}
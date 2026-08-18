package com.example.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.example.data.local.UserPreferences
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.AppLanguage
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = preferencesRepository.preferences

    fun setLanguage(language: AppLanguage) {
        preferencesRepository.setLanguage(language)
    }

    fun setDarkMode(enabled: Boolean, useSystem: Boolean = false) {
        preferencesRepository.setDarkMode(enabled, useSystem)
    }

    fun setLocation(location: String) {
        preferencesRepository.setLocation(location)
    }

    fun setFestivalNotification(enabled: Boolean) {
        preferencesRepository.setFestivalNotification(enabled)
    }

    fun setTempleEventNotification(enabled: Boolean) {
        preferencesRepository.setTempleEventNotification(enabled)
    }

    fun setPanchangamNotification(enabled: Boolean) {
        preferencesRepository.setPanchangamNotification(enabled)
    }

    fun setRasiPalanNotification(enabled: Boolean) {
        preferencesRepository.setRasiPalanNotification(enabled)
    }

    fun setFontSizeScale(scale: Float) {
        preferencesRepository.setFontSizeScale(scale)
    }
}

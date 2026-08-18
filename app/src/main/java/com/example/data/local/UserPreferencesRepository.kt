package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserPreferences(
    val language: AppLanguage = AppLanguage.TAMIL,
    val isDarkMode: Boolean = false,
    val useSystemTheme: Boolean = true,
    val selectedLocation: String = "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)",
    val festivalNotificationEnabled: Boolean = true,
    val templeEventNotificationEnabled: Boolean = true,
    val rasiPalanNotificationEnabled: Boolean = true,
    val panchangamNotificationEnabled: Boolean = true,
    val fontSizeScale: Float = 1.0f
)

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("kovil_app_prefs", Context.MODE_PRIVATE)

    private val _preferences = MutableStateFlow(loadPreferences())
    val preferences: StateFlow<UserPreferences> = _preferences.asStateFlow()

    private fun loadPreferences(): UserPreferences {
        val langCode = prefs.getString("key_language", AppLanguage.TAMIL.code) ?: AppLanguage.TAMIL.code
        val language = when (langCode) {
            AppLanguage.ENGLISH.code -> AppLanguage.ENGLISH
            AppLanguage.HINDI.code -> AppLanguage.HINDI
            else -> AppLanguage.TAMIL
        }
        val isDarkMode = prefs.getBoolean("key_dark_mode", false)
        val useSystemTheme = prefs.getBoolean("key_system_theme", true)
        val location = prefs.getString("key_location", "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)") ?: "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)"
        val festivalNotif = prefs.getBoolean("key_notif_festival", true)
        val templeNotif = prefs.getBoolean("key_notif_temple", true)
        val rasiNotif = prefs.getBoolean("key_notif_rasi", true)
        val panchangamNotif = prefs.getBoolean("key_notif_panchangam", true)
        val fontSize = prefs.getFloat("key_font_size", 1.0f)

        return UserPreferences(
            language = language,
            isDarkMode = isDarkMode,
            useSystemTheme = useSystemTheme,
            selectedLocation = location,
            festivalNotificationEnabled = festivalNotif,
            templeEventNotificationEnabled = templeNotif,
            rasiPalanNotificationEnabled = rasiNotif,
            panchangamNotificationEnabled = panchangamNotif,
            fontSizeScale = fontSize
        )
    }

    fun setLanguage(language: AppLanguage) {
        prefs.edit().putString("key_language", language.code).apply()
        _preferences.value = _preferences.value.copy(language = language)
    }

    fun setDarkMode(enabled: Boolean, useSystem: Boolean = false) {
        prefs.edit()
            .putBoolean("key_dark_mode", enabled)
            .putBoolean("key_system_theme", useSystem)
            .apply()
        _preferences.value = _preferences.value.copy(isDarkMode = enabled, useSystemTheme = useSystem)
    }

    fun setLocation(location: String) {
        prefs.edit().putString("key_location", location).apply()
        _preferences.value = _preferences.value.copy(selectedLocation = location)
    }

    fun setFestivalNotification(enabled: Boolean) {
        prefs.edit().putBoolean("key_notif_festival", enabled).apply()
        _preferences.value = _preferences.value.copy(festivalNotificationEnabled = enabled)
    }

    fun setTempleEventNotification(enabled: Boolean) {
        prefs.edit().putBoolean("key_notif_temple", enabled).apply()
        _preferences.value = _preferences.value.copy(templeEventNotificationEnabled = enabled)
    }

    fun setRasiPalanNotification(enabled: Boolean) {
        prefs.edit().putBoolean("key_notif_rasi", enabled).apply()
        _preferences.value = _preferences.value.copy(rasiPalanNotificationEnabled = enabled)
    }

    fun setPanchangamNotification(enabled: Boolean) {
        prefs.edit().putBoolean("key_notif_panchangam", enabled).apply()
        _preferences.value = _preferences.value.copy(panchangamNotificationEnabled = enabled)
    }

    fun setFontSizeScale(scale: Float) {
        prefs.edit().putFloat("key_font_size", scale).apply()
        _preferences.value = _preferences.value.copy(fontSizeScale = scale)
    }
}

package com.example.ui.screens.panchangam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.AppLanguage
import com.example.data.model.PanchangamDetail
import com.example.data.service.FijiTimeHelper
import com.example.data.service.PanchangamCalculator
import com.example.data.service.StandardPanchangamCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class PanchangamUiState(
    val selectedDate: LocalDate = FijiTimeHelper.todayInFiji(),
    val panchangamDetail: PanchangamDetail? = null,
    val selectedLocation: String = "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)",
    val language: AppLanguage = AppLanguage.TAMIL,
    val availableLocations: List<String> = listOf(
        "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)",
        "சுவா, பிஜி (Suva, Fiji Islands)",
        "லவுடோகா, பிஜி (Lautoka, Fiji Islands)",
        "லபாசா, பிஜி (Labasa, Fiji Islands)",
        "சென்னை (Chennai, India)",
        "மதுரை (Madurai, India)",
        "யாழ்ப்பாணம் (Jaffna, Sri Lanka)",
        "கொழும்பு (Colombo, Sri Lanka)",
        "சிங்கப்பூர் (Singapore)",
        "கோலாலம்பூர் (Kuala Lumpur, Malaysia)",
        "சிட்னி (Sydney, Australia)",
        "ஆக்லாந்து (Auckland, New Zealand)",
        "லண்டன் (London, UK)",
        "டொராண்டோ (Toronto, Canada)"
    )
)

class PanchangamViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val panchangamCalculator: PanchangamCalculator = StandardPanchangamCalculator()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PanchangamUiState())
    val uiState: StateFlow<PanchangamUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(
                    language = prefs.language,
                    selectedLocation = prefs.selectedLocation
                )
                calculateForDate(_uiState.value.selectedDate, prefs.selectedLocation)
            }
        }
    }

    fun setDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        calculateForDate(date, _uiState.value.selectedLocation)
    }

    fun nextDay() {
        val next = _uiState.value.selectedDate.plusDays(1)
        setDate(next)
    }

    fun previousDay() {
        val prev = _uiState.value.selectedDate.minusDays(1)
        setDate(prev)
    }

    fun setLocation(location: String) {
        preferencesRepository.setLocation(location)
        _uiState.value = _uiState.value.copy(selectedLocation = location)
        calculateForDate(_uiState.value.selectedDate, location)
    }

    private fun calculateForDate(date: LocalDate, location: String) {
        val detail = panchangamCalculator.calculatePanchangam(date, location)
        _uiState.value = _uiState.value.copy(panchangamDetail = detail)
    }
}

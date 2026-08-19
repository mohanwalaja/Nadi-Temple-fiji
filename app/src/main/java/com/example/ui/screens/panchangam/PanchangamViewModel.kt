package com.example.ui.screens.panchangam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.AppLanguage
import com.example.data.model.PanchangamDetail
import com.example.data.service.FijiTimeHelper
import com.example.data.service.LocationCoordinates
import com.example.data.service.PanchangamCalculator
import com.example.data.service.StandardPanchangamCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale

data class PanchangamUiState(
    val selectedDate: LocalDate = FijiTimeHelper.todayInFiji(),
    val panchangamDetail: PanchangamDetail? = null,
    val selectedLocation: String = "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)",
    val parsedLocation: LocationCoordinates? = null,
    val language: AppLanguage = AppLanguage.TAMIL,
    val availableLocations: List<LocationCoordinates> = StandardPanchangamCalculator.WORLD_PRESETS
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
                val parsed = panchangamCalculator.parseLocation(prefs.selectedLocation)
                _uiState.value = _uiState.value.copy(
                    language = prefs.language,
                    selectedLocation = prefs.selectedLocation,
                    parsedLocation = parsed
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
        val parsed = panchangamCalculator.parseLocation(location)
        _uiState.value = _uiState.value.copy(
            selectedLocation = location,
            parsedLocation = parsed
        )
        calculateForDate(_uiState.value.selectedDate, location)
    }

    fun setCoordinates(lat: Double, lon: Double, tzOffset: Double, placeName: String) {
        val formatted = String.format(
            Locale.US,
            "GPS: %.4f, %.4f, %.2f, %s",
            lat, lon, tzOffset, placeName
        )
        setLocation(formatted)
    }

    private fun calculateForDate(date: LocalDate, location: String) {
        val detail = panchangamCalculator.calculatePanchangam(date, location)
        val parsed = panchangamCalculator.parseLocation(location)
        _uiState.value = _uiState.value.copy(
            panchangamDetail = detail,
            parsedLocation = parsed
        )
    }
}

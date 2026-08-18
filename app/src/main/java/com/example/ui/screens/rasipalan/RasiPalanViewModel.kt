package com.example.ui.screens.rasipalan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.*
import com.example.data.repository.JathagamRepository
import com.example.data.repository.RasiPalanRepository
import com.example.data.service.RasiPalanEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RasiPalanUiState(
    val jathagam: HoroscopeResult? = null,
    val selectedRasi: Rasi? = null,
    val selectedTimeframe: PalanTimeframe = PalanTimeframe.YEARLY,
    val palanResult: RasiPalanResult? = null,
    val language: AppLanguage = AppLanguage.TAMIL
)

class RasiPalanViewModel(
    private val jathagamRepository: JathagamRepository,
    private val preferencesRepository: UserPreferencesRepository,
    private val rasiPalanRepository: RasiPalanRepository = RasiPalanRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RasiPalanUiState())
    val uiState: StateFlow<RasiPalanUiState> = _uiState.asStateFlow()

    init {
        // Collect current Jathagam from repository
        viewModelScope.launch {
            jathagamRepository.currentHoroscope.collect { horoscope ->
                if (horoscope != null) {
                    val rasi = horoscope.chandraRasi
                    _uiState.value = _uiState.value.copy(
                        jathagam = horoscope,
                        selectedRasi = rasi
                    )
                    updatePalan(horoscope, rasi, _uiState.value.selectedTimeframe)
                } else {
                    _uiState.value = _uiState.value.copy(jathagam = null)
                    updatePalan(null, _uiState.value.selectedRasi ?: Rasi.MESHAM, _uiState.value.selectedTimeframe)
                }
            }
        }

        // Collect language preferences
        viewModelScope.launch {
            preferencesRepository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(language = prefs.language)
            }
        }
    }

    fun selectRasi(rasi: Rasi?) {
        _uiState.value = _uiState.value.copy(selectedRasi = rasi)
        updatePalan(_uiState.value.jathagam, rasi, _uiState.value.selectedTimeframe)
    }

    fun selectTimeframe(timeframe: PalanTimeframe) {
        _uiState.value = _uiState.value.copy(selectedTimeframe = timeframe)
        updatePalan(_uiState.value.jathagam, _uiState.value.selectedRasi, timeframe)
    }

    private fun updatePalan(horoscope: HoroscopeResult?, selectedRasi: Rasi?, timeframe: PalanTimeframe) {
        val targetRasi = selectedRasi ?: horoscope?.chandraRasi ?: Rasi.MESHAM
        val result = if (horoscope != null && (selectedRasi == null || selectedRasi == horoscope.chandraRasi)) {
            RasiPalanEngine.generate(horoscope, timeframe)
        } else {
            RasiPalanEngine.generateForRasi(targetRasi, timeframe)
        }
        _uiState.value = _uiState.value.copy(palanResult = result)
    }
}

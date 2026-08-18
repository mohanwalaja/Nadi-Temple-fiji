package com.example.ui.screens.jathagam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferencesRepository
import com.example.data.local.entities.HoroscopeProfileEntity
import com.example.data.model.AppLanguage
import com.example.data.model.HoroscopeResult
import com.example.data.repository.JathagamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

data class JathagamUiState(
    val nameInput: String = "",
    val birthDate: LocalDate = LocalDate.of(1995, 6, 15),
    val birthTime: LocalTime = LocalTime.of(10, 30),
    val birthPlace: String = "நாடி (Nadi, Fiji)",
    val horoscopeResult: HoroscopeResult? = null,
    val savedProfiles: List<HoroscopeProfileEntity> = emptyList(),
    val isSavedSuccessfully: Boolean = false,
    val language: AppLanguage = AppLanguage.TAMIL
)

class JathagamViewModel(
    private val jathagamRepository: JathagamRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JathagamUiState())
    val uiState: StateFlow<JathagamUiState> = _uiState.asStateFlow()

    init {
        // Collect current calculated Jathagam from repository
        viewModelScope.launch {
            jathagamRepository.currentHoroscope.collect { result ->
                if (result != null) {
                    _uiState.value = _uiState.value.copy(
                        horoscopeResult = result,
                        nameInput = if (_uiState.value.nameInput.isBlank()) result.devoteeName else _uiState.value.nameInput,
                        birthDate = result.dob,
                        birthTime = result.tob,
                        birthPlace = result.birthPlace
                    )
                }
            }
        }

        // Collect language preferences
        viewModelScope.launch {
            preferencesRepository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(language = prefs.language)
            }
        }

        // Collect saved profiles
        viewModelScope.launch {
            jathagamRepository.getAllProfiles().collect { profiles ->
                _uiState.value = _uiState.value.copy(savedProfiles = profiles)
            }
        }

        // Initial calculation if no horoscope exists
        calculateHoroscope()
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(nameInput = name, isSavedSuccessfully = false)
    }

    fun onBirthDateChange(date: LocalDate) {
        _uiState.value = _uiState.value.copy(birthDate = date, isSavedSuccessfully = false)
    }

    fun onBirthTimeChange(time: LocalTime) {
        _uiState.value = _uiState.value.copy(birthTime = time, isSavedSuccessfully = false)
    }

    fun onBirthPlaceChange(place: String) {
        _uiState.value = _uiState.value.copy(birthPlace = place, isSavedSuccessfully = false)
    }

    fun resetToNew() {
        _uiState.value = _uiState.value.copy(
            nameInput = "",
            birthDate = LocalDate.of(1998, 1, 1),
            birthTime = LocalTime.of(9, 0),
            birthPlace = "நாடி (Nadi, Fiji)",
            isSavedSuccessfully = false
        )
    }

    fun calculateHoroscope() {
        val nameToUse = _uiState.value.nameInput.trim().ifBlank {
            if (_uiState.value.language == AppLanguage.TAMIL) "முருக பக்தர்" else "Devotee"
        }
        val result = jathagamRepository.calculateAndSet(
            name = nameToUse,
            dob = _uiState.value.birthDate,
            tob = _uiState.value.birthTime,
            birthPlace = _uiState.value.birthPlace.trim().ifBlank { "Nadi, Fiji" }
        )
        _uiState.value = _uiState.value.copy(horoscopeResult = result, isSavedSuccessfully = false)
    }

    fun saveCurrentProfile() {
        val result = _uiState.value.horoscopeResult ?: return
        viewModelScope.launch {
            val entity = HoroscopeProfileEntity(
                name = result.devoteeName,
                birthYear = result.dob.year,
                birthMonth = result.dob.monthValue,
                birthDay = result.dob.dayOfMonth,
                birthHour = result.tob.hour,
                birthMinute = result.tob.minute,
                birthPlace = result.birthPlace,
                lagnaRasiIndex = result.lagnaRasi.index,
                chandraRasiIndex = result.chandraRasi.index,
                nakshatram = result.janmaNakshatram,
                pada = result.janmaPada
            )
            jathagamRepository.saveProfile(entity)
            _uiState.value = _uiState.value.copy(isSavedSuccessfully = true)
        }
    }

    fun loadProfile(profile: HoroscopeProfileEntity) {
        val dob = LocalDate.of(profile.birthYear, profile.birthMonth, profile.birthDay)
        val tob = LocalTime.of(profile.birthHour, profile.birthMinute)
        _uiState.value = _uiState.value.copy(
            nameInput = profile.name,
            birthDate = dob,
            birthTime = tob,
            birthPlace = profile.birthPlace,
            isSavedSuccessfully = false
        )
        calculateHoroscope()
    }

    fun deleteProfile(profile: HoroscopeProfileEntity) {
        viewModelScope.launch {
            jathagamRepository.deleteProfile(profile)
        }
    }
}


package com.example.ui.screens.dharmasastra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.AppLanguage
import com.example.data.model.DharmaSastraDisclaimer
import com.example.data.model.SastraTopic
import com.example.data.repository.DharmaSastraRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DharmaSastraUiState(
    val topics: List<SastraTopic> = emptyList(),
    val disclaimer: DharmaSastraDisclaimer = DharmaSastraDisclaimer(),
    val language: AppLanguage = AppLanguage.TAMIL
)

class DharmaSastraViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val repository: DharmaSastraRepository = DharmaSastraRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DharmaSastraUiState())
    val uiState: StateFlow<DharmaSastraUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            topics = repository.getAllTopics(),
            disclaimer = repository.disclaimer
        )

        viewModelScope.launch {
            preferencesRepository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(language = prefs.language)
            }
        }
    }
}

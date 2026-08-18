package com.example.ui.screens.temple

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferencesRepository
import com.example.data.local.entities.SavedTempleEventEntity
import com.example.data.model.AppLanguage
import com.example.data.model.TempleContact
import com.example.data.model.TempleEvent
import com.example.data.model.TempleSchedule
import com.example.data.repository.TempleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class TempleUiState(
    val isTempleOpenNow: Boolean = true,
    val todayClosingTime: String = "7:00 PM",
    val schedule: TempleSchedule = TempleSchedule(),
    val contacts: List<TempleContact> = emptyList(),
    val events: List<TempleEvent> = emptyList(),
    val savedEventIds: Set<String> = emptySet(),
    val language: AppLanguage = AppLanguage.TAMIL
)

class TempleViewModel(
    private val templeRepository: TempleRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TempleUiState())
    val uiState: StateFlow<TempleUiState> = _uiState.asStateFlow()

    init {
        val now = LocalDate.now()
        val time = LocalTime.now()
        val currentDay = now.dayOfWeek

        val events = templeRepository.getTempleEvents(now)
        val isOpen = templeRepository.schedule.isTempleOpenNow(currentDay, time)
        val closing = templeRepository.schedule.getClosingTimeForDay(currentDay)

        _uiState.value = _uiState.value.copy(
            isTempleOpenNow = isOpen,
            todayClosingTime = closing,
            schedule = templeRepository.schedule,
            contacts = templeRepository.templeContacts,
            events = events
        )

        viewModelScope.launch {
            preferencesRepository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(language = prefs.language)
            }
        }

        viewModelScope.launch {
            templeRepository.savedEventsFlow?.collect { savedList ->
                val idSet = savedList.map { it.eventId }.toSet()
                _uiState.value = _uiState.value.copy(savedEventIds = idSet)
            }
        }
    }

    fun toggleEventReminder(eventId: String) {
        viewModelScope.launch {
            val isCurrentlySaved = _uiState.value.savedEventIds.contains(eventId)
            templeRepository.toggleEventSaved(eventId, isCurrentlySaved)
        }
    }
}

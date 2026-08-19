package com.example.ui.screens.babynames

import androidx.lifecycle.ViewModel
import com.example.data.model.NakshatraBabyLetters
import com.example.data.repository.BabyNamesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BabyNamesUiState(
    val selectedNakshatraIndex: Int = 1, // 1 to 27 (1 = Ashwini)
    val searchQuery: String = "",
    val viewMode: Int = 0, // 0 = Single Nakshatra Focus, 1 = All 27 Table View
    val currentNakshatraLetters: NakshatraBabyLetters? = null,
    val searchResults: List<NakshatraBabyLetters> = emptyList()
)

class BabyNamesViewModel(
    private val repository: BabyNamesRepository = BabyNamesRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        BabyNamesUiState(
            currentNakshatraLetters = repository.getNakshatraLetters(1),
            searchResults = repository.allNakshatraLetters
        )
    )
    val uiState: StateFlow<BabyNamesUiState> = _uiState.asStateFlow()

    val allNakshatraLetters: List<NakshatraBabyLetters> = repository.allNakshatraLetters

    fun selectNakshatra(index: Int) {
        _uiState.update { current ->
            current.copy(
                selectedNakshatraIndex = index,
                currentNakshatraLetters = repository.getNakshatraLetters(index)
            )
        }
    }

    fun updateSearchQuery(query: String) {
        val results = repository.searchNakshatraByQuery(query)
        _uiState.update { current ->
            current.copy(
                searchQuery = query,
                searchResults = results,
                currentNakshatraLetters = results.firstOrNull() ?: current.currentNakshatraLetters
            )
        }
    }

    fun setViewMode(mode: Int) {
        _uiState.update { it.copy(viewMode = mode) }
    }
}

package com.example.ui.screens.babynames

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.data.model.AppLanguage
import com.example.data.model.BabyNamingBirthResult
import com.example.data.model.HoroscopeResult
import com.example.data.model.NakshatraBabyLetters
import com.example.data.repository.BabyNamesRepository
import com.example.data.service.PrecisionLahiriAstrologyCalculator
import com.example.util.BabyNamePdfExporter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

data class BabyNamesUiState(
    // 0 = Find by Birth Details (DOB/TOB/Place), 1 = Nakshatra Browser, 2 = All 27 Panchangam Table
    val tabIndex: Int = 0,
    
    // Birth Input Fields
    val inputBabyName: String = "",
    val inputGender: String = "M", // "M" or "F"
    val inputDob: LocalDate = LocalDate.now(),
    val inputTob: LocalTime = LocalTime.of(10, 30),
    val inputPlace: String = "Chennai, Tamil Nadu",
    val isCalculating: Boolean = false,
    val birthResult: BabyNamingBirthResult? = null,
    val exportedPdfFile: File? = null,
    
    // Manual Nakshatra Browser
    val selectedNakshatraIndex: Int = 1,
    val searchQuery: String = "",
    val currentNakshatraLetters: NakshatraBabyLetters? = null,
    val searchResults: List<NakshatraBabyLetters> = emptyList()
)

class BabyNamesViewModel(
    private val repository: BabyNamesRepository = BabyNamesRepository(),
    private val astrologyCalculator: PrecisionLahiriAstrologyCalculator = PrecisionLahiriAstrologyCalculator()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        BabyNamesUiState(
            currentNakshatraLetters = repository.getNakshatraLetters(1),
            searchResults = repository.allNakshatraLetters
        )
    )
    val uiState: StateFlow<BabyNamesUiState> = _uiState.asStateFlow()

    val allNakshatraLetters: List<NakshatraBabyLetters> = repository.allNakshatraLetters

    init {
        // Run initial calculation for today's birth preview
        calculateByBirthDetails(
            babyName = "செல்வன்",
            dob = LocalDate.now(),
            tob = LocalTime.of(10, 30),
            place = "Chennai, Tamil Nadu",
            gender = "M"
        )
    }

    fun setTabIndex(index: Int) {
        _uiState.update { it.copy(tabIndex = index) }
    }

    fun updateInputBabyName(name: String) {
        _uiState.update { it.copy(inputBabyName = name) }
    }

    fun updateInputGender(gender: String) {
        _uiState.update { it.copy(inputGender = gender) }
        _uiState.value.birthResult?.let { res ->
            _uiState.update { it.copy(birthResult = res.copy(gender = gender)) }
        }
    }

    fun updateInputDob(dob: LocalDate) {
        _uiState.update { it.copy(inputDob = dob) }
    }

    fun updateInputTob(tob: LocalTime) {
        _uiState.update { it.copy(inputTob = tob) }
    }

    fun updateInputPlace(place: String) {
        _uiState.update { it.copy(inputPlace = place) }
    }

    fun calculateByBirthDetails(
        babyName: String = _uiState.value.inputBabyName,
        dob: LocalDate = _uiState.value.inputDob,
        tob: LocalTime = _uiState.value.inputTob,
        place: String = _uiState.value.inputPlace,
        gender: String = _uiState.value.inputGender
    ) {
        _uiState.update { it.copy(isCalculating = true) }
        try {
            val horoscope = astrologyCalculator.calculateHoroscope(
                name = babyName.ifBlank { "Baby" },
                dob = dob,
                tob = tob,
                birthPlace = place.ifBlank { "Chennai, Tamil Nadu" }
            )

            val nakshatraBabyLetters = repository.getByNakshatraName(horoscope.janmaNakshatram)
            val pada = horoscope.janmaPada.coerceIn(1, 4)
            val primaryPadaInfo = nakshatraBabyLetters.padas.firstOrNull { it.padaNumber == pada }
                ?: nakshatraBabyLetters.padas[0]

            val birthRes = BabyNamingBirthResult(
                babyName = babyName,
                gender = gender,
                dob = dob,
                tob = tob,
                birthPlace = place,
                nakshatraLetters = nakshatraBabyLetters,
                janmaPada = pada,
                primaryPadaInfo = primaryPadaInfo,
                chandraRasi = horoscope.chandraRasi,
                lagnaRasi = horoscope.lagnaRasi
            )

            _uiState.update {
                it.copy(
                    isCalculating = false,
                    birthResult = birthRes,
                    inputBabyName = babyName,
                    inputDob = dob,
                    inputTob = tob,
                    inputPlace = place,
                    inputGender = gender
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _uiState.update { it.copy(isCalculating = false) }
        }
    }

    fun loadFromHoroscopeResult(horoscope: HoroscopeResult, gender: String = "M") {
        val nakshatraBabyLetters = repository.getByNakshatraName(horoscope.janmaNakshatram)
        val pada = horoscope.janmaPada.coerceIn(1, 4)
        val primaryPadaInfo = nakshatraBabyLetters.padas.firstOrNull { it.padaNumber == pada }
            ?: nakshatraBabyLetters.padas[0]

        val birthRes = BabyNamingBirthResult(
            babyName = horoscope.devoteeName,
            gender = gender,
            dob = horoscope.dob,
            tob = horoscope.tob,
            birthPlace = horoscope.birthPlace,
            nakshatraLetters = nakshatraBabyLetters,
            janmaPada = pada,
            primaryPadaInfo = primaryPadaInfo,
            chandraRasi = horoscope.chandraRasi,
            lagnaRasi = horoscope.lagnaRasi
        )

        _uiState.update {
            it.copy(
                tabIndex = 0,
                birthResult = birthRes,
                inputBabyName = horoscope.devoteeName,
                inputDob = horoscope.dob,
                inputTob = horoscope.tob,
                inputPlace = horoscope.birthPlace,
                inputGender = gender
            )
        }
    }

    fun selectNakshatra(index: Int) {
        val letters = repository.getNakshatraLetters(index)
        _uiState.update { current ->
            current.copy(
                selectedNakshatraIndex = index,
                currentNakshatraLetters = letters
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

    fun exportPdfCertificate(context: Context, lang: AppLanguage): File? {
        val result = _uiState.value.birthResult ?: return null
        val file = BabyNamePdfExporter.exportBabyNamingCertificatePdf(context, result, lang)
        _uiState.update { it.copy(exportedPdfFile = file) }
        return file
    }
}

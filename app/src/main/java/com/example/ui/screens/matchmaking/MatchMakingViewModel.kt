package com.example.ui.screens.matchmaking

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.data.model.AppLanguage
import com.example.data.model.Graha
import com.example.data.model.Rasi
import com.example.data.model.WeddingMatchResult
import com.example.data.service.AstrologyCalculator
import com.example.data.service.MatchMakingCalculator
import com.example.data.service.PrecisionLahiriAstrologyCalculator
import com.example.data.service.WeddingMatchPdfData
import com.example.data.service.WeddingMatchPdfExporter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

data class MatchMakingUiState(
    // Bride Birth Inputs
    val brideName: String = "",
    val brideDob: LocalDate = LocalDate.of(1998, 5, 15),
    val brideTob: LocalTime = LocalTime.of(10, 30),
    val bridePlace: String = "Chennai",

    // Groom Birth Inputs
    val groomName: String = "",
    val groomDob: LocalDate = LocalDate.of(1995, 8, 20),
    val groomTob: LocalTime = LocalTime.of(6, 45),
    val groomPlace: String = "Chennai",

    // Calculated Astrological Parameters
    val brideRasi: Rasi = Rasi.MESHAM,
    val brideNakshatraIndex: Int = 0,
    val bridePada: Int = 1,
    val brideLagna: Rasi? = Rasi.KADAGAM,
    val brideMarsHouse: Int = 1,

    val groomRasi: Rasi = Rasi.SIMHAM,
    val groomNakshatraIndex: Int = 9,
    val groomPada: Int = 1,
    val groomLagna: Rasi? = Rasi.SIMHAM,
    val groomMarsHouse: Int = 1,

    val isCalculatedFromBirthDetails: Boolean = true,
    val result: WeddingMatchResult = MatchMakingCalculator.calculateWeddingMatch(
        brideRasi = Rasi.MESHAM,
        brideNakshatraIndex = 0,
        bridePada = 1,
        groomRasi = Rasi.SIMHAM,
        groomNakshatraIndex = 9,
        groomPada = 1,
        brideMarsHouse = 1,
        groomMarsHouse = 1
    ),
    val selectedDetailTab: Int = 0, // 0 = 10 Poruthams, 1 = Sevvay Dosham
    val isExportingPdf: Boolean = false,
    val exportedPdfFile: File? = null
)

class MatchMakingViewModel(
    private val astrologyCalculator: AstrologyCalculator = PrecisionLahiriAstrologyCalculator()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchMakingUiState())
    val uiState: StateFlow<MatchMakingUiState> = _uiState.asStateFlow()

    init {
        // Automatically calculate initial match from default birth details
        fetchAndCalculateCharts()
    }

    fun updateBrideName(name: String) {
        _uiState.update { it.copy(brideName = name) }
    }

    fun updateBrideDob(dob: LocalDate) {
        _uiState.update { it.copy(brideDob = dob) }
    }

    fun updateBrideTob(tob: LocalTime) {
        _uiState.update { it.copy(brideTob = tob) }
    }

    fun updateBridePlace(place: String) {
        _uiState.update { it.copy(bridePlace = place) }
    }

    fun updateGroomName(name: String) {
        _uiState.update { it.copy(groomName = name) }
    }

    fun updateGroomDob(dob: LocalDate) {
        _uiState.update { it.copy(groomDob = dob) }
    }

    fun updateGroomTob(tob: LocalTime) {
        _uiState.update { it.copy(groomTob = tob) }
    }

    fun updateGroomPlace(place: String) {
        _uiState.update { it.copy(groomPlace = place) }
    }

    /**
     * Calculate exact Vedic horoscope for both Bride and Groom:
     * - Fetches Moon Longitude -> Chandra Rasi, Janma Nakshatram, Pada
     * - Calculates Lagna (Ascendant)
     * - Evaluates Mars (செவ்வாய்) position & house to detect Sevvay Dosham automatically
     * - Calculates full 10 Poruthams & Dosha Samyam
     */
    fun fetchAndCalculateCharts() {
        val current = _uiState.value

        val brideChart = astrologyCalculator.calculateHoroscope(
            name = current.brideName.ifBlank { "மணமகள்" },
            dob = current.brideDob,
            tob = current.brideTob,
            birthPlace = current.bridePlace
        )

        val groomChart = astrologyCalculator.calculateHoroscope(
            name = current.groomName.ifBlank { "மணமகன்" },
            dob = current.groomDob,
            tob = current.groomTob,
            birthPlace = current.groomPlace
        )

        // Find Bride Nakshatram index (0..26)
        val bStarIdx = PrecisionLahiriAstrologyCalculator.NAK_TA.indexOfFirst {
            brideChart.janmaNakshatram.startsWith(it.substringBefore(" ("))
        }.coerceAtLeast(0)

        // Find Groom Nakshatram index (0..26)
        val gStarIdx = PrecisionLahiriAstrologyCalculator.NAK_TA.indexOfFirst {
            groomChart.janmaNakshatram.startsWith(it.substringBefore(" ("))
        }.coerceAtLeast(0)

        // Calculate Mars house relative to Lagna
        val brideMars = brideChart.planetPositions.firstOrNull { it.graha == Graha.CHEVVAI }
        val brideMarsHouse = if (brideMars != null) {
            ((brideMars.rasi.index - brideChart.lagnaRasi.index + 12) % 12) + 1
        } else 1

        val groomMars = groomChart.planetPositions.firstOrNull { it.graha == Graha.CHEVVAI }
        val groomMarsHouse = if (groomMars != null) {
            ((groomMars.rasi.index - groomChart.lagnaRasi.index + 12) % 12) + 1
        } else 1

        val matchResult = MatchMakingCalculator.calculateWeddingMatch(
            brideRasi = brideChart.chandraRasi,
            brideNakshatraIndex = bStarIdx,
            bridePada = brideChart.janmaPada,
            groomRasi = groomChart.chandraRasi,
            groomNakshatraIndex = gStarIdx,
            groomPada = groomChart.janmaPada,
            brideMarsHouse = brideMarsHouse,
            groomMarsHouse = groomMarsHouse
        )

        _uiState.update {
            it.copy(
                brideRasi = brideChart.chandraRasi,
                brideNakshatraIndex = bStarIdx,
                bridePada = brideChart.janmaPada,
                brideLagna = brideChart.lagnaRasi,
                brideMarsHouse = brideMarsHouse,

                groomRasi = groomChart.chandraRasi,
                groomNakshatraIndex = gStarIdx,
                groomPada = groomChart.janmaPada,
                groomLagna = groomChart.lagnaRasi,
                groomMarsHouse = groomMarsHouse,

                isCalculatedFromBirthDetails = true,
                result = matchResult
            )
        }
    }

    // Manual override handlers
    fun updateBrideRasi(rasi: Rasi) {
        _uiState.update { current ->
            val newState = current.copy(brideRasi = rasi)
            newState.copy(result = recalculate(newState))
        }
    }

    fun updateBrideNakshatra(index: Int) {
        _uiState.update { current ->
            val newState = current.copy(brideNakshatraIndex = index)
            newState.copy(result = recalculate(newState))
        }
    }

    fun updateBridePada(pada: Int) {
        _uiState.update { current ->
            val newState = current.copy(bridePada = pada)
            newState.copy(result = recalculate(newState))
        }
    }

    fun updateBrideMarsHouse(house: Int) {
        _uiState.update { current ->
            val newState = current.copy(brideMarsHouse = house)
            newState.copy(result = recalculate(newState))
        }
    }

    fun updateGroomRasi(rasi: Rasi) {
        _uiState.update { current ->
            val newState = current.copy(groomRasi = rasi)
            newState.copy(result = recalculate(newState))
        }
    }

    fun updateGroomNakshatra(index: Int) {
        _uiState.update { current ->
            val newState = current.copy(groomNakshatraIndex = index)
            newState.copy(result = recalculate(newState))
        }
    }

    fun updateGroomPada(pada: Int) {
        _uiState.update { current ->
            val newState = current.copy(groomPada = pada)
            newState.copy(result = recalculate(newState))
        }
    }

    fun updateGroomMarsHouse(house: Int) {
        _uiState.update { current ->
            val newState = current.copy(groomMarsHouse = house)
            newState.copy(result = recalculate(newState))
        }
    }

    fun setSelectedDetailTab(tab: Int) {
        _uiState.update { it.copy(selectedDetailTab = tab) }
    }

    private fun recalculate(state: MatchMakingUiState): WeddingMatchResult {
        return MatchMakingCalculator.calculateWeddingMatch(
            brideRasi = state.brideRasi,
            brideNakshatraIndex = state.brideNakshatraIndex,
            bridePada = state.bridePada,
            groomRasi = state.groomRasi,
            groomNakshatraIndex = state.groomNakshatraIndex,
            groomPada = state.groomPada,
            brideMarsHouse = state.brideMarsHouse,
            groomMarsHouse = state.groomMarsHouse
        )
    }

    fun exportAndSharePdf(context: Context, language: AppLanguage) {
        val current = _uiState.value
        val brideStarName = MatchMakingCalculator.NAKSHATRAM_NAMES_TA.getOrElse(current.brideNakshatraIndex) { "" }
        val groomStarName = MatchMakingCalculator.NAKSHATRAM_NAMES_TA.getOrElse(current.groomNakshatraIndex) { "" }

        val pdfData = WeddingMatchPdfData(
            brideName = current.brideName,
            brideDob = current.brideDob,
            brideTob = current.brideTob,
            bridePlace = current.bridePlace,
            brideRasi = current.brideRasi,
            brideNakshatraName = brideStarName,
            bridePada = current.bridePada,
            brideLagna = current.brideLagna,
            brideMarsHouse = current.brideMarsHouse,

            groomName = current.groomName,
            groomDob = current.groomDob,
            groomTob = current.groomTob,
            groomPlace = current.groomPlace,
            groomRasi = current.groomRasi,
            groomNakshatraName = groomStarName,
            groomPada = current.groomPada,
            groomLagna = current.groomLagna,
            groomMarsHouse = current.groomMarsHouse,

            result = current.result,
            language = language
        )

        val file = WeddingMatchPdfExporter.generatePdf(context, pdfData)
        _uiState.update { it.copy(exportedPdfFile = file) }
        WeddingMatchPdfExporter.sharePdf(context, file)
    }
}

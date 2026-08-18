package com.example.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.AppLanguage
import com.example.data.model.HinduFestival
import com.example.data.model.PanchangamDetail
import com.example.data.model.TempleSchedule
import com.example.data.repository.FestivalRepository
import com.example.data.service.FijiTimeHelper
import com.example.data.service.PanchangamCalculator
import com.example.data.service.StandardPanchangamCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class HomeUiState(
    val todayEnglishDate: String = "",
    val todayTamilDateStr: String = "",
    val todayTamilMonthStr: String = "",
    val todayTamilYearStr: String = "",
    val dayOfWeekStr: String = "",
    val tithiStr: String = "",
    val nakshatramStr: String = "",
    val todayObservanceStr: String = "",
    val isTempleOpen: Boolean = true,
    val templeClosingTimeToday: String = "7:00 PM",
    val morningArtiTime: String = "7:15 AM",
    val eveningArtiTime: String = "6:00 PM",
    val panchangamDetail: PanchangamDetail? = null,
    val upcomingFestivals: List<Pair<HinduFestival, LocalDate>> = emptyList(),
    val language: AppLanguage = AppLanguage.TAMIL,
    val fijiTimeString: String = ""
)

class HomeViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val panchangamCalculator: PanchangamCalculator = StandardPanchangamCalculator(),
    private val festivalRepository: FestivalRepository = FestivalRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val templeSchedule = TempleSchedule()

    init {
        viewModelScope.launch {
            preferencesRepository.preferences.collect { prefs ->
                refreshData(prefs.language, prefs.selectedLocation)
            }
        }
    }

    fun refreshData(language: AppLanguage = _uiState.value.language, location: String = "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)") {
        val now = FijiTimeHelper.todayInFiji()
        val currentTime = FijiTimeHelper.currentTimeInFiji()
        val currentDay = now.dayOfWeek

        val panchangam = panchangamCalculator.calculatePanchangam(now, location)
        val isOpen = templeSchedule.isTempleOpenNow(currentDay, currentTime)
        val closeTime = templeSchedule.getClosingTimeForDay(currentDay)
        val upcoming = festivalRepository.getUpcomingFestivals(now, 4)

        val engFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH)
        val engDate = now.format(engFormatter)

        val tamilMonthName = panchangam.tamilMonth.name(language)
        val tamilYearName = panchangam.tamilYear.displayName(language)
        val tamilDateFormatted = "$tamilMonthName ${panchangam.tamilDate} ($tamilYearName)"

        val primaryObservance = if (panchangam.specialObservances.isNotEmpty()) {
            panchangam.specialObservances.first().getDisplayName(language)
        } else {
            when (language) {
                AppLanguage.TAMIL -> "நித்திய திருவாலயம் வழிபாடு"
                AppLanguage.HINDI -> "नित्य मंदिर दर्शन एवं महाआरती"
                AppLanguage.ENGLISH -> "Daily Temple Darshan"
            }
        }

        val tillLabel = when (language) {
            AppLanguage.TAMIL -> "${panchangam.tithiEndTime} வரை"
            AppLanguage.HINDI -> "${panchangam.tithiEndTime} तक"
            AppLanguage.ENGLISH -> "till ${panchangam.tithiEndTime}"
        }
        val nakTillLabel = when (language) {
            AppLanguage.TAMIL -> "${panchangam.nakshatramEndTime} வரை"
            AppLanguage.HINDI -> "${panchangam.nakshatramEndTime} तक"
            AppLanguage.ENGLISH -> "till ${panchangam.nakshatramEndTime}"
        }

        val fijiFormattedTime = FijiTimeHelper.formatFijiTime(currentTime)

        _uiState.value = HomeUiState(
            todayEnglishDate = engDate,
            todayTamilDateStr = tamilDateFormatted,
            todayTamilMonthStr = tamilMonthName,
            todayTamilYearStr = tamilYearName,
            dayOfWeekStr = panchangam.getDayOfWeek(language),
            tithiStr = "${panchangam.getTithi(language)} ($tillLabel)",
            nakshatramStr = "${panchangam.getNakshatram(language)} ($nakTillLabel)",
            todayObservanceStr = primaryObservance,
            isTempleOpen = isOpen,
            templeClosingTimeToday = closeTime,
            morningArtiTime = templeSchedule.morningArtiTime,
            eveningArtiTime = templeSchedule.eveningArtiTime,
            panchangamDetail = panchangam,
            upcomingFestivals = upcoming,
            language = language,
            fijiTimeString = fijiFormattedTime
        )
    }

    fun toggleLanguage() {
        val newLang = when (_uiState.value.language) {
            AppLanguage.TAMIL -> AppLanguage.ENGLISH
            AppLanguage.ENGLISH -> AppLanguage.HINDI
            AppLanguage.HINDI -> AppLanguage.TAMIL
        }
        preferencesRepository.setLanguage(newLang)
    }
}

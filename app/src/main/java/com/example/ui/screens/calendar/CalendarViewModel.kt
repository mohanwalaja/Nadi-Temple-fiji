package com.example.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.AppLanguage
import com.example.data.model.HinduFestival
import com.example.data.model.PanchangamDetail
import com.example.data.model.TamilSamvatsaraEngine
import com.example.data.repository.FestivalRepository
import com.example.data.service.FijiTimeHelper
import com.example.data.service.PanchangamCalculator
import com.example.data.service.StandardPanchangamCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

data class CalendarDayItem(
    val date: LocalDate,
    val gregorianDay: Int,
    val tamilDate: Int,
    val tamilMonthName: String,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val hasFestival: Boolean,
    val hasObservance: Boolean,
    val observanceName: String? = null
)

data class CalendarUiState(
    val selectedDate: LocalDate = FijiTimeHelper.todayInFiji(),
    val currentYearMonth: YearMonth = YearMonth.from(FijiTimeHelper.todayInFiji()),
    val daysInMonthGrid: List<CalendarDayItem> = emptyList(),
    val selectedDayPanchangam: PanchangamDetail? = null,
    val selectedDayFestival: HinduFestival? = null,
    val allFestivals: List<HinduFestival> = emptyList(),
    val filterQuery: String = "",
    val language: AppLanguage = AppLanguage.TAMIL
)

class CalendarViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val panchangamCalculator: PanchangamCalculator = StandardPanchangamCalculator(),
    private val festivalRepository: FestivalRepository = FestivalRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.preferences.collect { prefs ->
                loadMonthData(_uiState.value.currentYearMonth, _uiState.value.selectedDate, prefs.language)
            }
        }
    }

    fun selectDate(date: LocalDate) {
        val ym = YearMonth.from(date)
        loadMonthData(ym, date, _uiState.value.language)
    }

    fun nextMonth() {
        val nextYm = _uiState.value.currentYearMonth.plusMonths(1)
        loadMonthData(nextYm, nextYm.atDay(1), _uiState.value.language)
    }

    fun previousMonth() {
        val prevYm = _uiState.value.currentYearMonth.minusMonths(1)
        loadMonthData(prevYm, prevYm.atDay(1), _uiState.value.language)
    }

    fun setFilterQuery(query: String) {
        _uiState.value = _uiState.value.copy(filterQuery = query)
    }

    private fun loadMonthData(yearMonth: YearMonth, selectedDate: LocalDate, language: AppLanguage) {
        val today = FijiTimeHelper.todayInFiji()
        val firstDayOfMonth = yearMonth.atDay(1)
        val daysInMonth = yearMonth.lengthOfMonth()
        val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0 for Sunday

        val days = mutableListOf<CalendarDayItem>()

        // Preceding days from last month
        val prevMonth = yearMonth.minusMonths(1)
        val prevDaysCount = prevMonth.lengthOfMonth()
        for (i in (startDayOfWeek - 1) downTo 0) {
            val date = prevMonth.atDay(prevDaysCount - i)
            val (tMonth, tDate) = TamilSamvatsaraEngine.getTamilDate(date)
            days.add(
                CalendarDayItem(
                    date = date,
                    gregorianDay = date.dayOfMonth,
                    tamilDate = tDate,
                    tamilMonthName = tMonth.name(language),
                    isCurrentMonth = false,
                    isToday = date == today,
                    hasFestival = false,
                    hasObservance = false
                )
            )
        }

        // Current month days
        for (day in 1..daysInMonth) {
            val date = yearMonth.atDay(day)
            val (tMonth, tDate) = TamilSamvatsaraEngine.getTamilDate(date)
            val panchangam = panchangamCalculator.calculatePanchangam(date)
            val hasObs = panchangam.specialObservances.isNotEmpty()
            val obsName = panchangam.specialObservances.firstOrNull()?.getDisplayName(language)

            days.add(
                CalendarDayItem(
                    date = date,
                    gregorianDay = day,
                    tamilDate = tDate,
                    tamilMonthName = tMonth.name(language),
                    isCurrentMonth = true,
                    isToday = date == today,
                    hasFestival = hasObs,
                    hasObservance = hasObs,
                    observanceName = obsName
                )
            )
        }

        // Trailing days to fill 42 cells (6 rows of 7)
        val nextMonth = yearMonth.plusMonths(1)
        var nextDay = 1
        while (days.size < 42) {
            val date = nextMonth.atDay(nextDay)
            val (tMonth, tDate) = TamilSamvatsaraEngine.getTamilDate(date)
            days.add(
                CalendarDayItem(
                    date = date,
                    gregorianDay = date.dayOfMonth,
                    tamilDate = tDate,
                    tamilMonthName = tMonth.name(language),
                    isCurrentMonth = false,
                    isToday = date == today,
                    hasFestival = false,
                    hasObservance = false
                )
            )
            nextDay++
        }

        val selectedPanchangam = panchangamCalculator.calculatePanchangam(selectedDate)
        val allFestivals = festivalRepository.getAllFestivals()

        _uiState.value = CalendarUiState(
            selectedDate = selectedDate,
            currentYearMonth = yearMonth,
            daysInMonthGrid = days,
            selectedDayPanchangam = selectedPanchangam,
            allFestivals = allFestivals,
            language = language
        )
    }
}

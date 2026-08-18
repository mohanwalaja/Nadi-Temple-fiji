package com.example.data.repository

import com.example.data.local.HoroscopeDao
import com.example.data.local.entities.HoroscopeProfileEntity
import com.example.data.model.HoroscopeResult
import com.example.data.model.JathagamResult
import com.example.data.service.AstrologyCalculator
import com.example.data.service.PrecisionLahiriAstrologyCalculator
import com.example.data.service.StandardAstrologyCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime

class JathagamRepository(
    private val horoscopeDao: HoroscopeDao,
    private val astrologyCalculator: AstrologyCalculator = PrecisionLahiriAstrologyCalculator()
) {
    private val _currentHoroscope = MutableStateFlow<HoroscopeResult?>(null)
    val currentHoroscope: StateFlow<HoroscopeResult?> = _currentHoroscope.asStateFlow()

    init {
        // Automatically calculate default Jathagam on launch
        calculateAndSet(
            name = "முருக பக்தர்",
            dob = LocalDate.of(1995, 6, 15),
            tob = LocalTime.of(10, 30),
            birthPlace = "சென்னை (Chennai)"
        )
    }

    fun setHoroscope(horoscope: HoroscopeResult) {
        _currentHoroscope.value = horoscope
    }

    fun calculateAndSet(
        name: String,
        dob: LocalDate,
        tob: LocalTime,
        birthPlace: String
    ): HoroscopeResult {
        val result = astrologyCalculator.calculateHoroscope(
            name = name.ifBlank { "Devotee" },
            dob = dob,
            tob = tob,
            birthPlace = birthPlace
        )
        _currentHoroscope.value = result
        return result
    }

    fun getAllProfiles(): Flow<List<HoroscopeProfileEntity>> = horoscopeDao.getAllProfiles()

    suspend fun saveProfile(entity: HoroscopeProfileEntity): Long = horoscopeDao.insertProfile(entity)

    suspend fun deleteProfile(entity: HoroscopeProfileEntity) = horoscopeDao.deleteProfile(entity)

    fun clearHoroscope() {
        _currentHoroscope.value = null
    }
}

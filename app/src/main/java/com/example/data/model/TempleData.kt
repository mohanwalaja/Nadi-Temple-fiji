package com.example.data.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class TempleSchedule(
    val openTime: LocalTime = LocalTime.of(6, 0),
    val closingTimes: Map<DayOfWeek, LocalTime> = mapOf(
        DayOfWeek.MONDAY to LocalTime.of(20, 0),
        DayOfWeek.TUESDAY to LocalTime.of(20, 0),
        DayOfWeek.WEDNESDAY to LocalTime.of(19, 0),
        DayOfWeek.THURSDAY to LocalTime.of(19, 0),
        DayOfWeek.FRIDAY to LocalTime.of(20, 0),
        DayOfWeek.SATURDAY to LocalTime.of(19, 0),
        DayOfWeek.SUNDAY to LocalTime.of(19, 0)
    ),
    val morningArtiTime: String = "7:15 AM",
    val eveningArtiTime: String = "6:00 PM"
) {
    fun isTempleOpenNow(currentDay: DayOfWeek, currentTime: LocalTime): Boolean {
        val closeTime = closingTimes[currentDay] ?: LocalTime.of(19, 0)
        return !currentTime.isBefore(openTime) && !currentTime.isAfter(closeTime)
    }

    fun getClosingTimeForDay(day: DayOfWeek): String {
        val time = closingTimes[day] ?: LocalTime.of(19, 0)
        return when (time.hour) {
            20 -> "8:00 PM"
            19 -> "7:00 PM"
            else -> "$time"
        }
    }
}

data class TempleContact(
    val roleTa: String,
    val roleEn: String,
    val nameTa: String,
    val nameEn: String,
    val phoneNumber: String,
    val roleHi: String = roleEn,
    val nameHi: String = nameEn
) {
    fun getRole(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> roleTa
        AppLanguage.HINDI -> roleHi
        AppLanguage.ENGLISH -> roleEn
    }
    fun getName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> nameTa
        AppLanguage.HINDI -> nameHi
        AppLanguage.ENGLISH -> nameEn
    }
}

data class TempleEvent(
    val id: String,
    val date: LocalDate,
    val startTime: String,
    val endTime: String,
    val poojaNameTa: String,
    val poojaNameEn: String,
    val descriptionTa: String,
    val descriptionEn: String,
    val deityTa: String,
    val deityEn: String,
    val isNotificationEnabled: Boolean = false,
    val poojaNameHi: String = poojaNameEn,
    val descriptionHi: String = descriptionEn,
    val deityHi: String = deityEn
) {
    fun getPoojaName(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> poojaNameTa
        AppLanguage.HINDI -> poojaNameHi
        AppLanguage.ENGLISH -> poojaNameEn
    }
    fun getDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> descriptionTa
        AppLanguage.HINDI -> descriptionHi
        AppLanguage.ENGLISH -> descriptionEn
    }
    fun getDeity(lang: AppLanguage): String = when (lang) {
        AppLanguage.TAMIL -> deityTa
        AppLanguage.HINDI -> deityHi
        AppLanguage.ENGLISH -> deityEn
    }
}

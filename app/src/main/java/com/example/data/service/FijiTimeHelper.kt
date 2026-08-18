package com.example.data.service

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Time utility helper calibrated for Nadi, Fiji Islands (UTC+12:00 / Pacific/Fiji).
 * Defaults app time, calendar events, poojas, and panchangam astronomical baselines
 * to the local time of Sri Siva Subramaniya Swami Kovil, Nadi, Fiji.
 */
object FijiTimeHelper {
    const val DEFAULT_LOCATION_NAME = "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)"
    const val DEFAULT_LOCATION_SHORT = "Nadi, Fiji Islands"
    const val TIMEZONE_ID_STR = "Pacific/Fiji"
    const val TIMEZONE_LABEL = "Fiji Time (UTC+12:00)"

    val FIJI_ZONE_ID: ZoneId by lazy {
        try {
            ZoneId.of(TIMEZONE_ID_STR)
        } catch (e: Exception) {
            ZoneId.of("GMT+12:00")
        }
    }

    fun nowInFiji(): ZonedDateTime = ZonedDateTime.now(FIJI_ZONE_ID)

    fun todayInFiji(): LocalDate = LocalDate.now(FIJI_ZONE_ID)

    fun currentTimeInFiji(): LocalTime = LocalTime.now(FIJI_ZONE_ID)

    fun formatFijiTime(time: LocalTime): String {
        return time.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
    }

    fun formatFijiDateTime(zdt: ZonedDateTime): String {
        return zdt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a (z)", Locale.ENGLISH))
    }
}

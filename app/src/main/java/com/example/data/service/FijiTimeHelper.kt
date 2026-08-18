package com.example.data.service

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Astronomical and Time utility helper calibrated for Nadi & Suva, Fiji Islands (UTC+12:00 / Pacific/Fiji)
 * and India (IST - UTC+05:30 / Asia/Kolkata).
 *
 * ASTRONOMICAL CALCULATION RULES:
 * 1. Panchangam events (Tithi, Nakshatram, Yoga, Karanam) occur at a single universal instant (UTC) globally.
 * 2. Fiji Standard Time (FJT) is UTC+12:00, while Indian Standard Time (IST) is UTC+05:30.
 * 3. Fiji is exactly 6 hours and 30 minutes AHEAD of India.
 * 4. Local solar events (Sunrise, Sunset, Rahu Kalam, Yamagandam, Suryodaya Tithi) are computed from local latitude/longitude.
 */
object FijiTimeHelper {
    const val DEFAULT_LOCATION_NAME = "நாடி, பிஜி தீவுகள் (Nadi, Fiji Islands)"
    const val DEFAULT_LOCATION_SHORT = "Nadi, Fiji Islands"
    const val TIMEZONE_ID_STR = "Pacific/Fiji"
    const val TIMEZONE_LABEL = "Fiji Time (UTC+12:00)"

    // Standard ZoneIds
    val FIJI_ZONE_ID: ZoneId by lazy {
        try { ZoneId.of(TIMEZONE_ID_STR) } catch (e: Exception) { ZoneOffset.ofHours(12) }
    }
    
    val INDIA_ZONE_ID: ZoneId by lazy {
        try { ZoneId.of("Asia/Kolkata") } catch (e: Exception) { ZoneOffset.ofHoursMinutes(5, 30) }
    }

    // Coordinates for Fiji
    const val NADI_LATITUDE = -17.80
    const val NADI_LONGITUDE = 177.41
    const val SUVA_LATITUDE = -18.14
    const val SUVA_LONGITUDE = 178.44

    fun nowInFiji(): ZonedDateTime = ZonedDateTime.now(FIJI_ZONE_ID)
    fun todayInFiji(): LocalDate = LocalDate.now(FIJI_ZONE_ID)
    fun currentTimeInFiji(): LocalTime = LocalTime.now(FIJI_ZONE_ID)

    fun nowInIndia(): ZonedDateTime = ZonedDateTime.now(INDIA_ZONE_ID)
    fun todayInIndia(): LocalDate = LocalDate.now(INDIA_ZONE_ID)

    /**
     * Converts an Indian Standard Time (IST) date and time to Fiji Standard Time (FJT).
     * Automatically handles date boundary rollover (+6h 30m offset).
     *
     * Example:
     * - India: 12:30 PM (same day) -> Fiji: 07:00 PM (same day)
     * - India: 09:00 PM (Oct 14) -> Fiji: 03:30 AM (Oct 15 - next day)
     */
    fun convertIstToFjt(istDate: LocalDate, istTime: LocalTime): ConvertedTimeResult {
        val istZdt = ZonedDateTime.of(istDate, istTime, INDIA_ZONE_ID)
        val fjtZdt = istZdt.withZoneSameInstant(FIJI_ZONE_ID)
        return ConvertedTimeResult(
            sourceDate = istDate,
            sourceTime = istTime,
            sourceZone = "IST (UTC+5:30)",
            targetDate = fjtZdt.toLocalDate(),
            targetTime = fjtZdt.toLocalTime(),
            targetZone = "FJT (UTC+12:00)",
            isNextDay = fjtZdt.toLocalDate().isAfter(istDate),
            formattedFjt = fjtZdt.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)),
            fullFormatted = fjtZdt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH))
        )
    }

    /**
     * Converts a Fiji Standard Time (FJT) date and time to Indian Standard Time (IST).
     * (-6h 30m offset).
     */
    fun convertFjtToIst(fjtDate: LocalDate, fjtTime: LocalTime): ConvertedTimeResult {
        val fjtZdt = ZonedDateTime.of(fjtDate, fjtTime, FIJI_ZONE_ID)
        val istZdt = fjtZdt.withZoneSameInstant(INDIA_ZONE_ID)
        return ConvertedTimeResult(
            sourceDate = fjtDate,
            sourceTime = fjtTime,
            sourceZone = "FJT (UTC+12:00)",
            targetDate = istZdt.toLocalDate(),
            targetTime = istZdt.toLocalTime(),
            targetZone = "IST (UTC+5:30)",
            isNextDay = istZdt.toLocalDate().isAfter(fjtDate),
            formattedFjt = istZdt.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)),
            fullFormatted = istZdt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH))
        )
    }

    /**
     * Converts a Universal Timestamp (Epoch Millis or Instant) to Local Fiji Time.
     */
    fun universalEpochToFjt(epochMilli: Long): ZonedDateTime {
        return Instant.ofEpochMilli(epochMilli).atZone(FIJI_ZONE_ID)
    }

    /**
     * Computes the 8 diurnal segments for inauspicious periods (Rahu Kalam, Yamagandam, Gulika)
     * based on local Fiji Sunrise and Sunset.
     */
    fun calculateDynamicRahuKalamFiji(
        dayOfWeek: DayOfWeek,
        sunrise: LocalTime,
        sunset: LocalTime
    ): TimeRange {
        val startMin = sunrise.toSecondOfDay() / 60
        val endMin = sunset.toSecondOfDay() / 60
        val duration = endMin - startMin
        val segment = duration / 8

        // Segment index (1-8) for Rahu Kalam:
        // Sun: 8, Mon: 2, Tue: 7, Wed: 5, Thu: 6, Fri: 4, Sat: 3
        val rahuSegment = when (dayOfWeek) {
            DayOfWeek.SUNDAY -> 8
            DayOfWeek.MONDAY -> 2
            DayOfWeek.TUESDAY -> 7
            DayOfWeek.WEDNESDAY -> 5
            DayOfWeek.THURSDAY -> 6
            DayOfWeek.FRIDAY -> 4
            DayOfWeek.SATURDAY -> 3
        }

        val rStart = startMin + (rahuSegment - 1) * segment
        val rEnd = startMin + rahuSegment * segment

        return TimeRange(
            start = LocalTime.ofSecondOfDay((rStart * 60).toLong()),
            end = LocalTime.ofSecondOfDay((rEnd * 60).toLong())
        )
    }

    fun formatFijiTime(time: LocalTime): String {
        return time.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
    }

    fun formatFijiDateTime(zdt: ZonedDateTime): String {
        return zdt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a (z)", Locale.ENGLISH))
    }

    data class ConvertedTimeResult(
        val sourceDate: LocalDate,
        val sourceTime: LocalTime,
        val sourceZone: String,
        val targetDate: LocalDate,
        val targetTime: LocalTime,
        val targetZone: String,
        val isNextDay: Boolean,
        val formattedFjt: String,
        val fullFormatted: String
    )

    data class TimeRange(
        val start: LocalTime,
        val end: LocalTime
    ) {
        fun format(): String {
            val fmt = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
            return "${start.format(fmt)} - ${end.format(fmt)}"
        }
    }
}


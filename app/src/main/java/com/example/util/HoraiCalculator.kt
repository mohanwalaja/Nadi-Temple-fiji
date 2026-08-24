package com.example.util

import com.example.data.model.HoraBlock
import com.example.data.model.HoraCalculationResult
import com.example.data.model.HoraRuler
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Expert Vedic Astrology Hora (Horai) Calculator.
 *
 * Implements classical Panchangam principles:
 * 1. 24 Horas per Vedic solar day (Ahoratra), beginning at Sunrise and ending at Next Sunrise.
 * 2. 12 Daytime Horas (Dina Hora) from Sunrise to Sunset.
 * 3. 12 Nighttime Horas (Ratri Hora) from Sunset to Next Sunrise.
 * 4. 1st Hora of the day is ruled by the Lord of the Vedic Day of Week (Vara Adhipati).
 * 5. Subsequent rulers cycle through the ancient Chaldean sequence:
 *    Sun -> Venus -> Mercury -> Moon -> Saturn -> Jupiter -> Mars -> (repeat).
 */
object HoraiCalculator {

    /**
     * The ancient Chaldean planetary order (from geocentric slowest to fastest/diminishing spheres):
     * Saturn, Jupiter, Mars, Sun, Venus, Mercury, Moon.
     *
     * In consecutive Horas, the sequence descends:
     * Sun -> Venus -> Mercury -> Moon -> Saturn -> Jupiter -> Mars -> (Sun...)
     */
    val CHALDEAN_ORDER: List<HoraRuler> = listOf(
        HoraRuler.SUN,
        HoraRuler.VENUS,
        HoraRuler.MERCURY,
        HoraRuler.MOON,
        HoraRuler.SATURN,
        HoraRuler.JUPITER,
        HoraRuler.MARS
    )

    /**
     * Maps the Gregorian/Vedic Day of the Week to its classical Vedic Day Lord (Vara Adhipati):
     * Sunday -> Sun
     * Monday -> Moon
     * Tuesday -> Mars
     * Wednesday -> Mercury
     * Thursday -> Jupiter
     * Friday -> Venus
     * Saturday -> Saturn
     */
    fun getVedicDayLord(dayOfWeek: DayOfWeek): HoraRuler {
        return when (dayOfWeek) {
            DayOfWeek.SUNDAY -> HoraRuler.SUN
            DayOfWeek.MONDAY -> HoraRuler.MOON
            DayOfWeek.TUESDAY -> HoraRuler.MARS
            DayOfWeek.WEDNESDAY -> HoraRuler.MERCURY
            DayOfWeek.THURSDAY -> HoraRuler.JUPITER
            DayOfWeek.FRIDAY -> HoraRuler.VENUS
            DayOfWeek.SATURDAY -> HoraRuler.SATURN
        }
    }

    /**
     * Calculates the exact 24 Hora time blocks for a given date and astronomical solar events.
     *
     * @param targetDate The target calendar date for which the sunrise occurs.
     * @param sunrise Local time of Sunrise on targetDate.
     * @param sunset Local time of Sunset on targetDate.
     * @param nextDaySunrise Local time of Sunrise on the next calendar day (targetDate + 1).
     * @return [HoraCalculationResult] containing exactly 24 calculated Hora blocks and metadata.
     */
    fun calculate24Horas(
        targetDate: LocalDate,
        sunrise: LocalTime,
        sunset: LocalTime,
        nextDaySunrise: LocalTime
    ): HoraCalculationResult {
        val dayOfWeek = targetDate.dayOfWeek
        val firstHoraRuler = getVedicDayLord(dayOfWeek)
        val firstRulerIndex = CHALDEAN_ORDER.indexOf(firstHoraRuler)

        // 1. Build precise LocalDateTime boundaries
        val sunriseDT = LocalDateTime.of(targetDate, sunrise)
        val sunsetDT = if (sunset.isBefore(sunrise)) {
            // Extreme polar anomaly safety fallback
            LocalDateTime.of(targetDate.plusDays(1), sunset)
        } else {
            LocalDateTime.of(targetDate, sunset)
        }
        val nextSunriseDT = LocalDateTime.of(targetDate.plusDays(1), nextDaySunrise)

        // 2. Exact durations
        val daytimeDurationSeconds = Duration.between(sunriseDT, sunsetDT).seconds
        val nighttimeDurationSeconds = Duration.between(sunsetDT, nextSunriseDT).seconds

        val dayHoraSeconds = daytimeDurationSeconds.toDouble() / 12.0
        val nightHoraSeconds = nighttimeDurationSeconds.toDouble() / 12.0

        val horasList = ArrayList<HoraBlock>(24)

        // 3. Generate 12 Daytime Horas (1 to 12)
        for (i in 0 until 12) {
            val ruler = CHALDEAN_ORDER[(firstRulerIndex + i) % CHALDEAN_ORDER.size]
            val startSecondsOffset = (i * dayHoraSeconds).toLong()
            val endSecondsOffset = ((i + 1) * dayHoraSeconds).toLong()

            val blockStartDT = sunriseDT.plusSeconds(startSecondsOffset)
            val blockEndDT = if (i == 11) sunsetDT else sunriseDT.plusSeconds(endSecondsOffset)

            val durationMinutes = Duration.between(blockStartDT, blockEndDT).toMillis() / 60000.0

            horasList.add(
                HoraBlock(
                    horaNumber = i + 1,
                    isDaytime = true,
                    startTime = blockStartDT.toLocalTime(),
                    endTime = blockEndDT.toLocalTime(),
                    startDateTime = blockStartDT,
                    endDateTime = blockEndDT,
                    durationMinutes = durationMinutes,
                    ruler = ruler
                )
            )
        }

        // 4. Generate 12 Nighttime Horas (13 to 24)
        for (j in 0 until 12) {
            val globalHoraIndex = 12 + j
            val ruler = CHALDEAN_ORDER[(firstRulerIndex + globalHoraIndex) % CHALDEAN_ORDER.size]
            val startSecondsOffset = (j * nightHoraSeconds).toLong()
            val endSecondsOffset = ((j + 1) * nightHoraSeconds).toLong()

            val blockStartDT = sunsetDT.plusSeconds(startSecondsOffset)
            val blockEndDT = if (j == 11) nextSunriseDT else sunsetDT.plusSeconds(endSecondsOffset)

            val durationMinutes = Duration.between(blockStartDT, blockEndDT).toMillis() / 60000.0

            horasList.add(
                HoraBlock(
                    horaNumber = globalHoraIndex + 1,
                    isDaytime = false,
                    startTime = blockStartDT.toLocalTime(),
                    endTime = blockEndDT.toLocalTime(),
                    startDateTime = blockStartDT,
                    endDateTime = blockEndDT,
                    durationMinutes = durationMinutes,
                    ruler = ruler
                )
            )
        }

        return HoraCalculationResult(
            targetDate = targetDate,
            dayOfWeek = dayOfWeek,
            vedicDayLord = firstHoraRuler,
            sunrise = sunrise,
            sunset = sunset,
            nextDaySunrise = nextDaySunrise,
            daytimeDurationMinutes = daytimeDurationSeconds / 60,
            nighttimeDurationMinutes = nighttimeDurationSeconds / 60,
            dayHoraDurationMinutes = dayHoraSeconds / 60.0,
            nightHoraDurationMinutes = nightHoraSeconds / 60.0,
            allHoras = horasList
        )
    }

    /**
     * Convenience overload taking string times (e.g. "06:15 AM", "06:30 PM").
     */
    fun calculate24Horas(
        targetDate: LocalDate,
        sunriseStr: String,
        sunsetStr: String,
        nextDaySunriseStr: String,
        timePattern: String = "hh:mm a"
    ): HoraCalculationResult {
        val formatter = DateTimeFormatter.ofPattern(timePattern, Locale.ENGLISH)
        val sunrise = LocalTime.parse(sunriseStr.trim().uppercase(Locale.ENGLISH), formatter)
        val sunset = LocalTime.parse(sunsetStr.trim().uppercase(Locale.ENGLISH), formatter)
        val nextDaySunrise = LocalTime.parse(nextDaySunriseStr.trim().uppercase(Locale.ENGLISH), formatter)

        return calculate24Horas(targetDate, sunrise, sunset, nextDaySunrise)
    }

    /**
     * Returns the active Hora for any given instant [currentTime].
     */
    fun getCurrentHora(
        targetDate: LocalDate,
        sunrise: LocalTime,
        sunset: LocalTime,
        nextDaySunrise: LocalTime,
        currentTime: LocalDateTime
    ): HoraBlock? {
        val result = calculate24Horas(targetDate, sunrise, sunset, nextDaySunrise)
        return result.getCurrentHora(currentTime)
    }

    /**
     * Returns all Auspicious (Shubha) Horas for the given day.
     */
    fun getAuspiciousHoras(
        targetDate: LocalDate,
        sunrise: LocalTime,
        sunset: LocalTime,
        nextDaySunrise: LocalTime
    ): List<HoraBlock> {
        val result = calculate24Horas(targetDate, sunrise, sunset, nextDaySunrise)
        return result.auspiciousHoras
    }
}

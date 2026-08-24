package com.example

import com.example.data.model.HoraNature
import com.example.data.model.HoraRuler
import com.example.util.HoraiCalculator
import org.junit.Assert.*
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class HoraiCalculatorTest {

    @Test
    fun testVedicDayLords() {
        assertEquals(HoraRuler.SUN, HoraiCalculator.getVedicDayLord(DayOfWeek.SUNDAY))
        assertEquals(HoraRuler.MOON, HoraiCalculator.getVedicDayLord(DayOfWeek.MONDAY))
        assertEquals(HoraRuler.MARS, HoraiCalculator.getVedicDayLord(DayOfWeek.TUESDAY))
        assertEquals(HoraRuler.MERCURY, HoraiCalculator.getVedicDayLord(DayOfWeek.WEDNESDAY))
        assertEquals(HoraRuler.JUPITER, HoraiCalculator.getVedicDayLord(DayOfWeek.THURSDAY))
        assertEquals(HoraRuler.VENUS, HoraiCalculator.getVedicDayLord(DayOfWeek.FRIDAY))
        assertEquals(HoraRuler.SATURN, HoraiCalculator.getVedicDayLord(DayOfWeek.SATURDAY))
    }

    @Test
    fun testSundayHoraCycleAndCount() {
        // Sunday: 2026-08-23
        val sunday = LocalDate.of(2026, 8, 23)
        assertEquals(DayOfWeek.SUNDAY, sunday.dayOfWeek)

        val sunrise = LocalTime.of(6, 0)
        val sunset = LocalTime.of(18, 0)
        val nextSunrise = LocalTime.of(6, 0)

        val result = HoraiCalculator.calculate24Horas(sunday, sunrise, sunset, nextSunrise)

        // 1. Total count must be 24
        assertEquals(24, result.allHoras.size)
        assertEquals(12, result.daytimeHoras.size)
        assertEquals(12, result.nighttimeHoras.size)

        // 2. First Hora ruler must be Sun on Sunday
        assertEquals(HoraRuler.SUN, result.allHoras[0].ruler)
        assertEquals(1, result.allHoras[0].horaNumber)
        assertTrue(result.allHoras[0].isDaytime)
        assertEquals(LocalTime.of(6, 0), result.allHoras[0].startTime)
        assertEquals(LocalTime.of(7, 0), result.allHoras[0].endTime)

        // 3. Chaldean sequence checks: Sun -> Venus -> Mercury -> Moon -> Saturn -> Jupiter -> Mars -> Sun
        val expectedSundayDayRulers = listOf(
            HoraRuler.SUN,
            HoraRuler.VENUS,
            HoraRuler.MERCURY,
            HoraRuler.MOON,
            HoraRuler.SATURN,
            HoraRuler.JUPITER,
            HoraRuler.MARS,
            HoraRuler.SUN,
            HoraRuler.VENUS,
            HoraRuler.MERCURY,
            HoraRuler.MOON,
            HoraRuler.SATURN
        )

        for (i in 0 until 12) {
            assertEquals("Daytime Hora ${i + 1} ruler mismatch", expectedSundayDayRulers[i], result.daytimeHoras[i].ruler)
        }

        // Nighttime Hora 13 (index 12) follows Saturn -> Jupiter
        assertEquals(HoraRuler.JUPITER, result.allHoras[12].ruler)
        assertEquals(HoraRuler.MARS, result.allHoras[13].ruler)

        // 24th Hora on Sunday: (0 + 23) % 7 = 2 (Mercury)
        assertEquals(HoraRuler.MERCURY, result.allHoras[23].ruler)

        // The 25th Hora (i.e., next day sunrise) will naturally be Moon (Monday Day Lord)!
        val nextDayFirstRuler = HoraiCalculator.CHALDEAN_ORDER[(0 + 24) % 7]
        assertEquals(HoraRuler.MOON, nextDayFirstRuler)
    }

    @Test
    fun testUnequalDayAndNightDurations() {
        // Summer day: Sunrise 05:30 AM, Sunset 18:30 (13 hours day = 780 min -> 65 min per hora)
        // Night: 18:30 to 05:30 (11 hours night = 660 min -> 55 min per hora)
        val targetDate = LocalDate.of(2026, 6, 21) // Thursday
        val sunrise = LocalTime.of(5, 30)
        val sunset = LocalTime.of(18, 30)
        val nextSunrise = LocalTime.of(5, 30)

        val result = HoraiCalculator.calculate24Horas(targetDate, sunrise, sunset, nextSunrise)

        assertEquals(65.0, result.dayHoraDurationMinutes, 0.01)
        assertEquals(55.0, result.nightHoraDurationMinutes, 0.01)

        // Day Hora 1: 05:30 to 06:35
        assertEquals(LocalTime.of(5, 30), result.allHoras[0].startTime)
        assertEquals(LocalTime.of(6, 35), result.allHoras[0].endTime)

        // Day Hora 12 ends exactly at sunset (18:30)
        assertEquals(LocalTime.of(18, 30), result.allHoras[11].endTime)

        // Night Hora 24 ends exactly at next sunrise (05:30)
        assertEquals(LocalTime.of(5, 30), result.allHoras[23].endTime)
    }

    @Test
    fun testFridayShukraHora() {
        // Friday: 2026-08-28
        val friday = LocalDate.of(2026, 8, 28)
        assertEquals(DayOfWeek.FRIDAY, friday.dayOfWeek)

        val result = HoraiCalculator.calculate24Horas(
            friday,
            LocalTime.of(6, 15),
            LocalTime.of(18, 15),
            LocalTime.of(6, 15)
        )

        // 1st Hora must be Venus
        assertEquals(HoraRuler.VENUS, result.allHoras[0].ruler)
        assertEquals(HoraNature.AUSPICIOUS, result.allHoras[0].nature)
        assertTrue(result.allHoras[0].isAuspicious)

        // Current Hora lookup
        val midDay = LocalDateTime.of(friday, LocalTime.of(6, 30))
        val currentHora = result.getCurrentHora(midDay)
        assertNotNull(currentHora)
        assertEquals(1, currentHora?.horaNumber)
        assertEquals(HoraRuler.VENUS, currentHora?.ruler)
    }
}

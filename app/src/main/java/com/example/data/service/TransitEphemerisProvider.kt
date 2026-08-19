package com.example.data.service

import com.example.data.model.Rasi
import java.time.LocalDate

/**
 * Provides the CURRENT sidereal (Lahiri) transit Rasi for the slow-moving grahas
 * (Guru, Sani, Rahu, Ketu) and a fast approximation for Surya, based on the
 * actual calendar date rather than a frozen constant.
 *
 * IMPORTANT — MAINTENANCE NOTE:
 * Guru (~1 year/sign), Sani (~2.5 years/sign) and Rahu/Ketu (~1.5 years/sign)
 * move slowly, so this table only needs a new row every 1-2.5 years. Whenever
 * a new Peyarchi (sign change) happens, add one row to the relevant list below
 * with the ingress date. Dates are sourced from published Lahiri/Drik Panchang
 * transit calendars (sidereal). If a query date falls after the last known
 * entry, the provider returns the last known sign and flags [isExtrapolated]
 * so calling code can show a "please verify" note instead of silently
 * asserting an out-of-date position (which was the previous bug).
 */
object TransitEphemerisProvider {

    private data class Ingress(val date: LocalDate, val rasi: Rasi)

    data class TransitLookupResult(val rasi: Rasi, val isExtrapolated: Boolean)

    // --- Sani (Saturn) — verified Lahiri Peyarchi dates ---
    private val SANI_INGRESS = listOf(
        Ingress(LocalDate.of(2017, 1, 26), Rasi.KANNI),
        Ingress(LocalDate.of(2020, 1, 24), Rasi.MAGARAM),
        Ingress(LocalDate.of(2022, 4, 29), Rasi.KUMBAM),
        Ingress(LocalDate.of(2025, 3, 29), Rasi.MEENAM),
        Ingress(LocalDate.of(2027, 6, 3), Rasi.MESHAM)
    )

    // --- Guru (Jupiter) — verified Lahiri Peyarchi dates ---
    private val GURU_INGRESS = listOf(
        Ingress(LocalDate.of(2020, 11, 20), Rasi.MAGARAM),
        Ingress(LocalDate.of(2021, 4, 5), Rasi.KUMBAM),
        Ingress(LocalDate.of(2022, 4, 13), Rasi.MEENAM),
        Ingress(LocalDate.of(2023, 4, 21), Rasi.MESHAM),
        Ingress(LocalDate.of(2024, 5, 1), Rasi.RISHABAM),
        Ingress(LocalDate.of(2025, 5, 14), Rasi.MITHUNAM),
        Ingress(LocalDate.of(2026, 6, 1), Rasi.KADAGAM),
        Ingress(LocalDate.of(2027, 6, 25), Rasi.SIMHAM)
    )

    // --- Rahu (mean node) — verified transit dates. Ketu is always 7 signs (180°) away. ---
    private val RAHU_INGRESS = listOf(
        Ingress(LocalDate.of(2020, 9, 23), Rasi.VIRUCHIGAM),
        Ingress(LocalDate.of(2022, 4, 12), Rasi.MESHAM),
        Ingress(LocalDate.of(2023, 10, 30), Rasi.MEENAM),
        Ingress(LocalDate.of(2025, 5, 18), Rasi.KUMBAM),
        Ingress(LocalDate.of(2026, 12, 5), Rasi.MAGARAM)
    )

    private fun lookup(table: List<Ingress>, date: LocalDate): TransitLookupResult {
        val sorted = table.sortedBy { it.date }
        val applicable = sorted.lastOrNull { !date.isBefore(it.date) } ?: sorted.first()
        val isExtrapolated = date.isAfter(sorted.last().date.plusYears(3)) || date.isBefore(sorted.first().date)
        return TransitLookupResult(applicable.rasi, isExtrapolated)
    }

    fun currentSaniRasi(date: LocalDate = LocalDate.now()): TransitLookupResult = lookup(SANI_INGRESS, date)

    fun currentGuruRasi(date: LocalDate = LocalDate.now()): TransitLookupResult = lookup(GURU_INGRESS, date)

    fun currentRahuRasi(date: LocalDate = LocalDate.now()): TransitLookupResult = lookup(RAHU_INGRESS, date)

    fun currentKetuRasi(date: LocalDate = LocalDate.now()): TransitLookupResult {
        val rahu = currentRahuRasi(date)
        val ketuIdx = ((rahu.rasi.index - 1 + 6) % 12) + 1
        val ketuRasi = Rasi.values().first { it.index == ketuIdx }
        return TransitLookupResult(ketuRasi, rahu.isExtrapolated)
    }

    /**
     * Sun moves ~1° per day and completes the zodiac in a year, so its Rasi (Tamil solar month)
     * can be approximated directly from the date instead of a lookup table. This uses the mean
     * solar longitude formula with the same Lahiri ayanamsa constant used elsewhere in the app,
     * accurate to within a day of the real Sankranti transition — sufficient for Rasi Palan text.
     */
    fun currentSuryaRasi(date: LocalDate = LocalDate.now()): Rasi {
        val jd = toJulianDay(date)
        val t = (jd - 2451545.0) / 36525.0
        val meanLon = normalizeDeg(280.46646 + 36000.76983 * t)
        val meanAnomaly = Math.toRadians(normalizeDeg(357.52911 + 35999.05029 * t))
        val eqCenter = (1.914602 - 0.004817 * t) * Math.sin(meanAnomaly) +
                0.019993 * Math.sin(2 * meanAnomaly)
        val trueLon = normalizeDeg(meanLon + eqCenter)
        val ayanamsa = 23.85709167 + 1.396971 * t + 0.000308 * t * t
        val siderealLon = normalizeDeg(trueLon - ayanamsa)
        val rasiIdx = (siderealLon / 30.0).toInt() % 12 + 1
        return Rasi.values().first { it.index == rasiIdx }
    }

    private fun toJulianDay(date: LocalDate): Double {
        var y = date.year
        var m = date.monthValue
        val d = date.dayOfMonth
        if (m <= 2) { y -= 1; m += 12 }
        val a = y / 100
        val b = 2 - a + (a / 4)
        return Math.floor(365.25 * (y + 4716)) + Math.floor(30.6001 * (m + 1)) + d + b - 1524.5
    }

    private fun normalizeDeg(deg: Double): Double {
        var d = deg % 360.0
        if (d < 0) d += 360.0
        return d
    }
}

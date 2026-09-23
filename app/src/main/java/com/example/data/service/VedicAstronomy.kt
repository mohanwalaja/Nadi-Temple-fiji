package com.example.data.service

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 * Shared Lahiri / Meeus helpers so the horoscope, panchangam and Tamil-year
 * engines use one ayanamsa, one Sun and one Moon.
 *
 * The ascendant formula is the eastern-horizon form. Writing
 * atan2(-cos RAMC, sin RAMC * cos ε + tan φ * sin ε) returns the descendant
 * (exactly 180° off) and must not be used for Lagna.
 */
object VedicAstronomy {

    /**
     * Indian Astronomical Ephemeris / Swiss Ephemeris Chitrapaksha (Lahiri)
     * mean ayanamsa. 23°51'25.53" at J2000.0, IAU 1976 general precession.
     * [t] is Julian centuries from J2000.0 (JD 2451545.0).
     */
    fun lahiriAyanamsaDegrees(t: Double): Double {
        return 23.85709167 + 1.396971278 * t + 0.0003086 * t * t
    }

    fun normalizeDegrees(deg: Double): Double {
        var d = deg % 360.0
        if (d < 0.0) d += 360.0
        return d
    }

    /**
     * Tropical ecliptic longitude of the ascendant (eastern horizon).
     * [ramcDegrees] is local sidereal time in degrees, [latitudeDegrees] is
     * geographic latitude (south negative), [obliquityDegrees] is the obliquity.
     */
    fun tropicalAscendant(
        ramcDegrees: Double,
        latitudeDegrees: Double,
        obliquityDegrees: Double
    ): Double {
        val ramc = Math.toRadians(ramcDegrees)
        val eps = Math.toRadians(obliquityDegrees)
        val lat = Math.toRadians(latitudeDegrees)
        val y = cos(ramc)
        val x = -(sin(ramc) * cos(eps) + tan(lat) * sin(eps))
        return normalizeDegrees(Math.toDegrees(atan2(y, x)))
    }

    /** Meeus apparent geocentric tropical longitude of the Sun. */
    fun apparentSunTropical(t: Double): Double {
        val l0 = normalizeDegrees(280.46646 + 36000.76983 * t + 0.0003032 * t * t)
        val m = normalizeDegrees(357.52911 + 35999.05029 * t - 0.0001537 * t * t)
        val mRad = Math.toRadians(m)
        val c = (1.914602 - 0.004817 * t - 0.000014 * t * t) * sin(mRad) +
                (0.019993 - 0.000101 * t) * sin(2 * mRad) +
                0.000289 * sin(3 * mRad)
        val omega = Math.toRadians(125.04 - 1934.136 * t)
        return normalizeDegrees(l0 + c - 0.00569 - 0.00478 * sin(omega))
    }

    /**
     * Meeus lunar longitude (Table 47.A), terms through ~0.005°.
     * Accurate to a few arcminutes — enough for nakshatra pada away from the boundary,
     * and identical in the horoscope and panchangam engines.
     */
    fun moonTropicalLongitude(t: Double): Double {
        val l = normalizeDegrees(218.3164477 + 481267.88123421 * t - 0.0015786 * t * t + (t * t * t) / 538841.0)
        val d = Math.toRadians(normalizeDegrees(297.8501921 + 445267.1114034 * t - 0.0018819 * t * t))
        val m = Math.toRadians(normalizeDegrees(357.5291092 + 35999.0502909 * t - 0.0001536 * t * t))
        val mp = Math.toRadians(normalizeDegrees(134.9633964 + 477198.8675055 * t + 0.0087414 * t * t))
        val f = Math.toRadians(normalizeDegrees(93.2720950 + 483202.0175233 * t - 0.0036539 * t * t))

        // D, M, M', F, coefficient in 1e-6 degrees.
        val terms = arrayOf(
            intArrayOf(0, 0, 1, 0, 6288774),
            intArrayOf(2, 0, -1, 0, 1274027),
            intArrayOf(2, 0, 0, 0, 658314),
            intArrayOf(0, 0, 2, 0, 213618),
            intArrayOf(0, 1, 0, 0, -185116),
            intArrayOf(0, 0, 0, 2, -114332),
            intArrayOf(2, 0, -2, 0, 58793),
            intArrayOf(2, -1, -1, 0, 57066),
            intArrayOf(2, 0, 1, 0, 53322),
            intArrayOf(2, -1, 0, 0, 45758),
            intArrayOf(0, 1, -1, 0, -40923),
            intArrayOf(1, 0, 0, 0, -34720),
            intArrayOf(0, 1, 1, 0, -30383),
            intArrayOf(2, 0, 0, -2, 15327),
            intArrayOf(0, 0, 1, 2, -12528),
            intArrayOf(0, 0, 1, -2, 10980),
            intArrayOf(4, 0, -1, 0, 10675),
            intArrayOf(0, 0, 3, 0, 10034),
            intArrayOf(4, 0, -2, 0, 8548),
            intArrayOf(2, 1, -1, 0, -7888),
            intArrayOf(2, 1, 0, 0, -6766),
            intArrayOf(1, 0, -1, 0, -5163),
            intArrayOf(1, 1, 0, 0, 4987),
            intArrayOf(2, -1, 1, 0, 4036),
            intArrayOf(2, 0, 2, 0, 3994),
            intArrayOf(4, 0, 0, 0, 3861),
            intArrayOf(2, 0, -3, 0, 3665),
            intArrayOf(0, 1, -2, 0, -2689),
            intArrayOf(2, 0, -1, 2, -2602),
            intArrayOf(2, -1, -2, 0, 2390),
            intArrayOf(1, 0, 1, 0, -2348),
            intArrayOf(2, -2, 0, 0, 2236)
        )
        var sum = 0.0
        for (term in terms) {
            val arg = term[0] * d + term[1] * m + term[2] * mp + term[3] * f
            sum += term[4] * sin(arg)
        }
        return normalizeDegrees(l + sum / 1_000_000.0)
    }

    /** Meeus principal terms for lunar ecliptic latitude, used for moonrise. */
    fun moonLatitude(t: Double): Double {
        val d = Math.toRadians(normalizeDegrees(297.8501921 + 445267.1114034 * t - 0.0018819 * t * t))
        val mp = Math.toRadians(normalizeDegrees(134.9633964 + 477198.8675055 * t + 0.0087414 * t * t))
        val f = Math.toRadians(normalizeDegrees(93.2720950 + 483202.0175233 * t - 0.0036539 * t * t))
        return 5.128189 * sin(f) +
                0.280606 * sin(mp + f) +
                0.277693 * sin(mp - f) +
                0.173238 * sin(2 * d - f) +
                0.055413 * sin(2 * d + f - mp) +
                0.046272 * sin(2 * d - f - mp) +
                0.032573 * sin(2 * d + f) +
                0.017198 * sin(2 * mp + f)
    }

    /**
     * Mean tropical longitude of the lunar ascending node (Rahu).
     * Lahiri / IAE and the app's transit table use the mean node, not the true node.
     */
    fun meanRahuTropical(t: Double): Double {
        return normalizeDegrees(125.0445479 - 1934.1362891 * t + 0.0020754 * t * t + (t * t * t) / 467441.0)
    }

    /**
     * Civil UTC offset at [date]/[time] for a coordinate, including historical DST.
     * Falls back to [fallbackOffsetHours] when the region is not recognised.
     */
    fun offsetHoursAt(
        lat: Double,
        lon: Double,
        fallbackOffsetHours: Double,
        date: LocalDate,
        time: LocalTime
    ): Double {
        val zone = zoneIdFor(lat, lon) ?: return fallbackOffsetHours
        return try {
            val zdt = ZonedDateTime.of(date, time, ZoneId.of(zone))
            zdt.offset.totalSeconds / 3600.0
        } catch (_: Exception) {
            fallbackOffsetHours
        }
    }

    /**
     * IANA zone for preset birthplaces and GPS fixes in those regions.
     * Half-hour zones (India +5:30) and daylight saving (Fiji through 2021,
     * Australia, UK, US) cannot be recovered from longitude alone.
     */
    fun zoneIdFor(lat: Double, lon: Double): String? = when {
        lat in -19.5..-12.0 && lon in 176.0..180.0 -> "Pacific/Fiji"
        lat in -47.5..-34.0 && lon in 166.0..179.0 -> "Pacific/Auckland"
        lat in -33.0..-31.0 && lon in 115.0..116.5 -> "Australia/Perth"
        lat in -28.5..-26.5 && lon in 152.5..153.5 -> "Australia/Brisbane"
        lat in -38.5..-37.2 && lon in 144.4..145.6 -> "Australia/Melbourne"
        lat in -34.5..-33.2 && lon in 150.5..151.6 -> "Australia/Sydney"
        lat in 1.15..1.50 && lon in 103.6..104.1 -> "Asia/Singapore"
        lat in 2.8..6.6 && lon in 99.5..104.5 -> "Asia/Kuala_Lumpur"
        lat in 24.0..26.5 && lon in 54.0..56.0 -> "Asia/Dubai"
        lat in 25.1..25.5 && lon in 51.3..51.7 -> "Asia/Qatar"
        lat in 23.4..23.8 && lon in 58.2..58.6 -> "Asia/Muscat"
        lat in 25.9..26.3 && lon in 50.4..50.8 -> "Asia/Bahrain"
        lat in 29.1..29.6 && lon in 47.7..48.2 -> "Asia/Kuwait"
        lat in 24.5..25.0 && lon in 46.4..47.0 -> "Asia/Riyadh"
        // Sri Lanka before the broad India box (both are +5:30, but keep the right zone).
        lat in 5.8..10.0 && lon in 79.4..81.9 -> "Asia/Colombo"
        lat in 6.0..37.2 && lon in 68.0..97.5 -> "Asia/Kolkata"
        lat in 51.2..51.8 && lon in -0.6..0.3 -> "Europe/London"
        lat in 48.6..49.1 && lon in 2.1..2.6 -> "Europe/Paris"
        lat in 43.5..43.9 && lon in -79.6..-79.1 -> "America/Toronto"
        lat in 49.1..49.4 && lon in -123.3..-122.9 -> "America/Vancouver"
        lat in 40.5..40.95 && lon in -74.3..-73.7 -> "America/New_York"
        lat in 37.6..37.95 && lon in -122.6..-122.2 -> "America/Los_Angeles"
        lat in 33.8..34.3 && lon in -118.6..-118.0 -> "America/Los_Angeles"
        lat in 41.6..42.1 && lon in -87.9..-87.4 -> "America/Chicago"
        lat in 32.5..33.1 && lon in -97.1..-96.5 -> "America/Chicago"
        else -> null
    }
}

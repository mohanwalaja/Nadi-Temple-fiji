package com.example

import com.example.data.model.Rasi
import com.example.data.service.TransitEphemerisProvider
import com.example.data.service.VedicAstronomy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class VedicAstronomyAndTransitTest {

    @Test
    fun equatorRamcZeroRisesAtAriesNotLibra() {
        val asc = VedicAstronomy.tropicalAscendant(0.0, 0.0, 23.439291)
        assertEquals(90.0, asc, 0.01)
    }

    @Test
    fun fijiHistoricalDaylightSaving() {
        val summer2019 = VedicAstronomy.offsetHoursAt(
            -17.80, 177.41, 12.0, LocalDate.of(2019, 12, 15), LocalTime.of(10, 0)
        )
        val september2026 = VedicAstronomy.offsetHoursAt(
            -17.80, 177.41, 12.0, LocalDate.of(2026, 9, 22), LocalTime.NOON
        )
        assertEquals(13.0, summer2019, 0.01)
        assertEquals(12.0, september2026, 0.01)
    }

    @Test
    fun sydneyAndLondonUseDaylightSaving() {
        val sydneyJan = VedicAstronomy.offsetHoursAt(
            -33.87, 151.21, 10.0, LocalDate.of(2026, 1, 15), LocalTime.NOON
        )
        val sydneyJul = VedicAstronomy.offsetHoursAt(
            -33.87, 151.21, 10.0, LocalDate.of(2026, 7, 15), LocalTime.NOON
        )
        val londonJul = VedicAstronomy.offsetHoursAt(
            51.51, -0.13, 0.0, LocalDate.of(2026, 7, 15), LocalTime.of(8, 30)
        )
        assertEquals(11.0, sydneyJan, 0.01)
        assertEquals(10.0, sydneyJul, 0.01)
        assertEquals(1.0, londonJul, 0.01)
    }

    @Test
    fun jupiterIngressIncludesThe2026LeoWindow() {
        assertEquals(Rasi.KADAGAM, TransitEphemerisProvider.currentGuruRasi(LocalDate.of(2026, 9, 22)).rasi)
        assertEquals(Rasi.SIMHAM, TransitEphemerisProvider.currentGuruRasi(LocalDate.of(2026, 11, 15)).rasi)
        assertEquals(Rasi.KADAGAM, TransitEphemerisProvider.currentGuruRasi(LocalDate.of(2027, 2, 1)).rasi)
        assertEquals(Rasi.SIMHAM, TransitEphemerisProvider.currentGuruRasi(LocalDate.of(2027, 7, 1)).rasi)
    }

    @Test
    fun meanRahuWasInTaurusNotScorpioIn2021() {
        assertEquals(Rasi.RISHABAM, TransitEphemerisProvider.currentRahuRasi(LocalDate.of(2021, 6, 1)).rasi)
        assertEquals(Rasi.KUMBAM, TransitEphemerisProvider.currentRahuRasi(LocalDate.of(2026, 9, 22)).rasi)
    }

    @Test
    fun lahiriAyanamsaMatchesIaeAtJ2000() {
        assertEquals(23.85709167, VedicAstronomy.lahiriAyanamsaDegrees(0.0), 1e-8)
        assertTrue(kotlin.math.abs(VedicAstronomy.meanRahuTropical(0.0) - 125.0445479) < 1e-6)
    }
}

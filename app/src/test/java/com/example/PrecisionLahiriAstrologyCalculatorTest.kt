package com.example

import com.example.data.model.Graha
import com.example.data.model.Rasi
import com.example.data.service.PrecisionLahiriAstrologyCalculator
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class PrecisionLahiriAstrologyCalculatorTest {

    private val calculator = PrecisionLahiriAstrologyCalculator()

    @Test
    fun testHoroscopeCalculationReturnsValidChart() {
        val result = calculator.calculateHoroscope(
            name = "முருக பக்தர்",
            dob = LocalDate.of(1995, 6, 15),
            tob = LocalTime.of(10, 30),
            birthPlace = "நாடி (Nadi, Fiji)"
        )

        assertNotNull(result)
        assertEquals("முருக பக்தர்", result.devoteeName)
        assertNotNull(result.lagnaRasi)
        assertNotNull(result.chandraRasi)
        assertTrue(result.janmaPada in 1..4)
        assertTrue(result.janmaNakshatram.isNotBlank())
        assertEquals(9, result.planetPositions.size)
        assertEquals(12, result.bhavas.size)
        assertEquals(9, result.navamsaPositions.size)
        assertTrue(result.dashaPeriods.isNotEmpty())
        assertFalse(result.isDemoEngine)
    }

    @Test
    fun testNineGrahasAssignedToValidRasis() {
        val result = calculator.calculateHoroscope(
            name = "Devotee",
            dob = LocalDate.of(2000, 1, 1),
            tob = LocalTime.of(12, 0),
            birthPlace = "சென்னை (Chennai)"
        )

        val grahas = result.planetPositions.map { it.graha }.toSet()
        assertEquals(9, grahas.size)
        assertTrue(grahas.contains(Graha.SURYA))
        assertTrue(grahas.contains(Graha.CHANDRA))
        assertTrue(grahas.contains(Graha.RAHU))
        assertTrue(grahas.contains(Graha.KETU))

        val rahu = result.planetPositions.first { it.graha == Graha.RAHU }
        val ketu = result.planetPositions.first { it.graha == Graha.KETU }
        val separation = kotlin.math.abs(longitude(rahu) - longitude(ketu))
        assertEquals(180.0, separation, 0.05)
    }

    @Test
    fun lagnaIsEasternHorizonNotDescendant() {
        // Chennai, 1 Jan 2000, 12:00 IST. The old atan2(-cos RAMC, +x) formula
        // returned Kanni (descendant). The rising sign is Meenam ~15.8°.
        val result = calculator.calculateHoroscope(
            name = "Lagna",
            dob = LocalDate.of(2000, 1, 1),
            tob = LocalTime.of(12, 0),
            birthPlace = "சென்னை (Chennai)"
        )
        assertEquals(Rasi.MEENAM, result.lagnaRasi)
        assertTrue("lagna ${result.lagnaDegrees}", result.lagnaDegrees in 14.5..17.5)
    }

    @Test
    fun nadiMorningLagnaIsKadagam() {
        val result = calculator.calculateHoroscope(
            name = "Nadi",
            dob = LocalDate.of(1995, 6, 15),
            tob = LocalTime.of(10, 30),
            birthPlace = "நாடி (Nadi, Fiji)"
        )
        assertEquals(Rasi.KADAGAM, result.lagnaRasi)
        assertTrue("lagna ${result.lagnaDegrees}", result.lagnaDegrees in 25.0..28.5)
    }

    @Test
    fun fijiDaylightSavingShiftsLagna() {
        // 15 Dec 2019 Nadi was UTC+13, not the preset +12. +12 puts Makara at ~29°; +13 at ~15°.
        val result = calculator.calculateHoroscope(
            name = "DST",
            dob = LocalDate.of(2019, 12, 15),
            tob = LocalTime.of(10, 0),
            birthPlace = "Nadi, Fiji"
        )
        assertEquals(Rasi.MAGARAM, result.lagnaRasi)
        assertTrue("expected ~15° Makara with Fiji +13, got ${result.lagnaDegrees}", result.lagnaDegrees in 12.0..18.0)
    }

    @Test
    fun kalaSarpaIsNotLabeledPartial() {
        val result = calculator.calculateHoroscope(
            name = "Dosha",
            dob = LocalDate.of(1995, 6, 15),
            tob = LocalTime.of(10, 30),
            birthPlace = "Nadi, Fiji"
        )
        val kala = result.doshas.first { it.nameEn.contains("Kala Sarpa") }
        assertFalse(kala.severityEn.contains("Partial"))
        val pitru = result.doshas.first { it.nameEn.contains("Pitru") }
        assertTrue(pitru.severityEn == "Pitru Dosha indicated" || pitru.severityEn == "Auspicious Status")
    }

    private fun longitude(p: com.example.data.model.PlanetPosition): Double {
        return (p.rasi.index - 1) * 30.0 + p.degrees
    }
}

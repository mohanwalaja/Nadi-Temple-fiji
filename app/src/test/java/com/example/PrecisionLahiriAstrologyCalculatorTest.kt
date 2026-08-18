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
    }
}

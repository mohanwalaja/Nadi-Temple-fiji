package com.example

import com.example.data.model.TamilMonth
import com.example.data.service.StandardPanchangamCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class PanchangamCalculatorTest {

    private val calculator = StandardPanchangamCalculator()

    @Test
    fun january2026IsStillThePreviousTamilYear() {
        val day = calculator.calculatePanchangam(
            LocalDate.of(2026, 1, 10), -17.7765, 177.4356, 12.0, "Nadi"
        )
        assertEquals("விசுவாசுவ", day.tamilYear.tamilName)
        assertEquals(TamilMonth.MARGHAZHI, day.tamilMonth)
        assertTrue(day.tamilDate in 1..31)
        assertTrue(day.tithiPercent in 0..100)
    }

    @Test
    fun april14BeforeMeshaSankrantiKeepsVisvavasu() {
        val day = calculator.calculatePanchangam(
            LocalDate.of(2026, 4, 14), -17.7765, 177.4356, 12.0, "Nadi"
        )
        assertEquals("விசுவாசுவ", day.tamilYear.tamilName)
        assertEquals(TamilMonth.PANGUNI, day.tamilMonth)
    }

    @Test
    fun april20IsParabhavaChithirai() {
        val day = calculator.calculatePanchangam(
            LocalDate.of(2026, 4, 20), -17.7765, 177.4356, 12.0, "Nadi"
        )
        assertEquals("பராபவ", day.tamilYear.tamilName)
        assertEquals(TamilMonth.CHITHIRAI, day.tamilMonth)
        assertTrue("tamil day ${day.tamilDate}", day.tamilDate in 4..10)
    }

    @Test
    fun tithiPercentIsElapsedNotHardcoded() {
        val a = calculator.calculatePanchangam(LocalDate.of(2026, 1, 10), "Nadi, Fiji")
        val b = calculator.calculatePanchangam(LocalDate.of(2026, 1, 18), "Nadi, Fiji")
        assertTrue(a.tithiPercent in 0..100)
        assertTrue(b.tithiPercent in 0..100)
        assertTrue(a.tithiPercent != 100 || b.tithiPercent != 100)
        assertTrue(a.sunrise.endsWith("AM"))
    }
}

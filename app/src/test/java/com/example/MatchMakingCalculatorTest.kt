package com.example

import com.example.data.model.PoruthamStatus
import com.example.data.model.Rasi
import com.example.data.service.MatchMakingCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MatchMakingCalculatorTest {

    @Test
    fun kujaCancellationUsesMarsSignNotMoonSign() {
        // Lagna Taurus, Mars in the 2nd = Gemini. That cancels, even if the Moon is elsewhere.
        val cancelled = MatchMakingCalculator.calculateWeddingMatch(
            brideRasi = Rasi.MESHAM,
            brideNakshatraIndex = 0,
            bridePada = 1,
            groomRasi = Rasi.SIMHAM,
            groomNakshatraIndex = 9,
            groomPada = 1,
            brideMarsHouse = 2,
            groomMarsHouse = 1,
            brideLagna = Rasi.RISHABAM,
            brideMarsRasi = Rasi.MITHUNAM
        )
        assertFalse(cancelled.sevvayDosham.isBrideHasDosham)

        // Old bug: Moon in Gemini + Mars in the 2nd cancelled, even when Mars was in Taurus.
        val notCancelled = MatchMakingCalculator.calculateWeddingMatch(
            brideRasi = Rasi.MITHUNAM,
            brideNakshatraIndex = 6,
            bridePada = 1,
            groomRasi = Rasi.SIMHAM,
            groomNakshatraIndex = 9,
            groomPada = 1,
            brideMarsHouse = 2,
            groomMarsHouse = 1,
            brideLagna = Rasi.MESHAM,
            brideMarsRasi = Rasi.RISHABAM
        )
        assertTrue(notCancelled.sevvayDosham.isBrideHasDosham)
    }

    @Test
    fun moonReferenceAlsoFormsKujaDosha() {
        // Mars in the 1st from Taurus lagna (no lagna dosha) is the 8th from Libra Moon.
        val result = MatchMakingCalculator.calculateWeddingMatch(
            brideRasi = Rasi.THULAM,
            brideNakshatraIndex = 15,
            bridePada = 1,
            groomRasi = Rasi.SIMHAM,
            groomNakshatraIndex = 9,
            groomPada = 1,
            brideMarsHouse = 1,
            groomMarsHouse = 1,
            brideLagna = Rasi.RISHABAM,
            brideMarsRasi = Rasi.RISHABAM
        )
        assertTrue(result.sevvayDosham.isBrideHasDosham)
    }

    @Test
    fun rasiAdhipathiUsesNaturalFriendship() {
        val enemies = match(Rasi.MESHAM, Rasi.MITHUNAM)
        val friends = match(Rasi.MESHAM, Rasi.SIMHAM)
        val sameLord = match(Rasi.MESHAM, Rasi.VIRUCHIGAM)
        assertEquals(PoruthamStatus.PORUNDHADHU, porutham(enemies, "rasiyadhipathi"))
        assertEquals(PoruthamStatus.UTTHAMAM, porutham(friends, "rasiyadhipathi"))
        assertEquals(PoruthamStatus.UTTHAMAM, porutham(sameLord, "rasiyadhipathi"))
    }

    private fun match(bride: Rasi, groom: Rasi) = MatchMakingCalculator.calculateWeddingMatch(
        brideRasi = bride,
        brideNakshatraIndex = 0,
        bridePada = 1,
        groomRasi = groom,
        groomNakshatraIndex = 9,
        groomPada = 1,
        brideMarsHouse = 1,
        groomMarsHouse = 1
    )

    private fun porutham(result: com.example.data.model.WeddingMatchResult, id: String) =
        result.poruthams.first { it.id == id }.status
}

package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.*
import com.example.data.service.RasiPalanEngine
import com.example.data.service.StandardAstrologyCalculator
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate
import java.time.LocalTime

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Sri Siva Subramaniya Swami Kovil", appName)
  }

  @Test
  fun `rasi palan engine generates predictions from calculated jathagam`() {
    val calc = StandardAstrologyCalculator()
    val jathagam = calc.calculateHoroscope(
      name = "Devotee Test",
      dob = LocalDate.of(1995, 6, 15),
      tob = LocalTime.of(10, 30),
      birthPlace = "Nadi, Fiji"
    )

    assertNotNull(jathagam.janmaRasi)
    val palanResult = RasiPalanEngine.generate(jathagam, PalanTimeframe.DAILY)

    assertEquals(jathagam.janmaRasi, palanResult.rasi)
    assertEquals(jathagam.janmaNakshatram, palanResult.janmaNakshatram)
    assertTrue(palanResult.generalTa.isNotEmpty())
    assertTrue(palanResult.generalEn.isNotEmpty())
    assertTrue(palanResult.pariharamTa.contains("ஸ்ரீ சிவ சுப்பிரமணிய"))
  }
}


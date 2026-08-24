package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.BabyNamesRepository
import com.example.ui.screens.babynames.BabyNamesViewModel
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
  fun `baby naming view model calculates nakshatra and pada letters`() {
    val repo = BabyNamesRepository()
    val viewModel = BabyNamesViewModel(repository = repo)
    viewModel.calculateByBirthDetails(
      babyName = "Murugan Child",
      dob = LocalDate.of(2026, 8, 23),
      tob = LocalTime.of(10, 30),
      place = "Nadi, Fiji",
      gender = "M"
    )

    val state = viewModel.uiState.value
    assertNotNull(state.birthResult)
    val result = state.birthResult!!
    assertNotNull(result.nakshatraLetters)
    assertTrue(result.janmaPada in 1..4)
    assertNotNull(result.primaryPadaInfo)
    assertTrue(result.primaryPadaInfo.letterTa.isNotBlank())
    assertTrue(result.primaryPadaInfo.letterEn.isNotBlank())
  }
}

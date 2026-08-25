package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.local.AppDatabase
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.AppLanguage
import com.example.data.repository.BabyNamesRepository
import com.example.data.repository.JathagamRepository
import com.example.data.service.AstrologyCalculator
import com.example.data.service.PrecisionLahiriAstrologyCalculator
import com.example.ui.components.TempleBottomBar
import com.example.ui.components.TempleTopBar
import com.example.ui.navigation.Screen
import com.example.ui.screens.babynames.BabyNamesScreen
import com.example.ui.screens.babynames.BabyNamesViewModel
import com.example.ui.screens.jathagam.JathagamScreen
import com.example.ui.screens.jathagam.JathagamViewModel
import com.example.ui.screens.matchmaking.MatchMakingScreen
import com.example.ui.screens.matchmaking.MatchMakingViewModel
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.settings.SettingsViewModel
import com.example.ui.theme.SriSivaKovilTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(this)
        val preferencesRepository = UserPreferencesRepository(this)
        val astrologyCalculator: AstrologyCalculator = PrecisionLahiriAstrologyCalculator()
        val jathagamRepository = JathagamRepository(database.horoscopeDao(), astrologyCalculator)
        val babyNamesRepository = BabyNamesRepository()

        setContent {
            val userPrefs by preferencesRepository.preferences.collectAsState()

            SriSivaKovilTheme(
                darkTheme = if (userPrefs.useSystemTheme) isSystemInDarkTheme() else userPrefs.isDarkMode
            ) {
                MainAppContainer(
                    preferencesRepository = preferencesRepository,
                    database = database,
                    astrologyCalculator = astrologyCalculator,
                    jathagamRepository = jathagamRepository,
                    babyNamesRepository = babyNamesRepository
                )
            }
        }
    }
}

@Composable
fun MainAppContainer(
    preferencesRepository: UserPreferencesRepository,
    database: AppDatabase,
    astrologyCalculator: AstrologyCalculator,
    jathagamRepository: JathagamRepository,
    babyNamesRepository: BabyNamesRepository
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val userPrefs by preferencesRepository.preferences.collectAsState()
    val currentLanguage = userPrefs.language

    // Instantiate ViewModels
    val jathagamViewModel = remember { JathagamViewModel(jathagamRepository, preferencesRepository) }
    val matchMakingViewModel = remember { MatchMakingViewModel() }
    val babyNamesViewModel = remember { BabyNamesViewModel(babyNamesRepository) }
    val settingsViewModel = remember { SettingsViewModel(preferencesRepository) }

    val isTopLevelRoute = currentRoute in listOf(
        Screen.Jathagam.route,
        Screen.WeddingMatch.route,
        Screen.BabyNames.route
    )

    val isDark = if (userPrefs.useSystemTheme) isSystemInDarkTheme() else userPrefs.isDarkMode

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isTopLevelRoute) {
                TempleTopBar(
                    currentLanguage = currentLanguage,
                    onToggleLanguage = {
                        val nextLang = when (currentLanguage) {
                            AppLanguage.TAMIL -> AppLanguage.ENGLISH
                            else -> AppLanguage.TAMIL
                        }
                        preferencesRepository.setLanguage(nextLang)
                    },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                    isDarkMode = isDark,
                    onToggleDarkMode = {
                        preferencesRepository.setDarkMode(!isDark, useSystem = false)
                    }
                )
            }
        },
        bottomBar = {
            if (isTopLevelRoute) {
                TempleBottomBar(
                    currentRoute = currentRoute,
                    currentLanguage = currentLanguage,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Jathagam.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Jathagam.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Jathagam (Horoscope) Screen (Primary Start Screen)
            composable(Screen.Jathagam.route) {
                JathagamScreen(
                    viewModel = jathagamViewModel,
                    onNavigateToBabyNames = {
                        val currentHoroscope = jathagamViewModel.uiState.value.horoscopeResult
                        if (currentHoroscope != null) {
                            babyNamesViewModel.loadFromHoroscopeResult(currentHoroscope)
                        }
                        navController.navigate(Screen.BabyNames.route) {
                            popUpTo(Screen.Jathagam.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            // 2. Wedding Match (திருமணப் பொருத்தம் & செவ்வாய் தோஷம்) Screen
            composable(Screen.WeddingMatch.route) {
                MatchMakingScreen(
                    viewModel = matchMakingViewModel,
                    currentLanguage = currentLanguage
                )
            }

            // 3. Baby Names & Naming Letters by Birth Details Screen
            composable(Screen.BabyNames.route) {
                BabyNamesScreen(
                    viewModel = babyNamesViewModel,
                    currentLanguage = currentLanguage
                )
            }

            // 4. Settings Screen
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

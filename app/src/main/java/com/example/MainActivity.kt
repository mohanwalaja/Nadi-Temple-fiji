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
import com.example.data.repository.RasiPalanRepository
import com.example.data.service.AstrologyCalculator
import com.example.data.service.PrecisionLahiriAstrologyCalculator
import com.example.data.service.StandardPanchangamCalculator
import com.example.ui.components.TempleBottomBar
import com.example.ui.components.TempleTopBar
import com.example.ui.navigation.Screen
import com.example.ui.screens.babynames.BabyNamesScreen
import com.example.ui.screens.babynames.BabyNamesViewModel
import com.example.ui.screens.jathagam.JathagamScreen
import com.example.ui.screens.jathagam.JathagamViewModel
import com.example.ui.screens.matchmaking.MatchMakingScreen
import com.example.ui.screens.matchmaking.MatchMakingViewModel
import com.example.ui.screens.panchangam.PanchangamScreen
import com.example.ui.screens.panchangam.PanchangamViewModel
import com.example.ui.screens.rasipalan.RasiPalanScreen
import com.example.ui.screens.rasipalan.RasiPalanViewModel
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.settings.SettingsViewModel
import com.example.ui.theme.SriSivaKovilTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(this)
        val preferencesRepository = UserPreferencesRepository(this)
        val panchangamCalculator = StandardPanchangamCalculator()
        val astrologyCalculator: AstrologyCalculator = PrecisionLahiriAstrologyCalculator()
        val rasiPalanRepository = RasiPalanRepository()
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
                    panchangamCalculator = panchangamCalculator,
                    astrologyCalculator = astrologyCalculator,
                    rasiPalanRepository = rasiPalanRepository,
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
    panchangamCalculator: StandardPanchangamCalculator,
    astrologyCalculator: AstrologyCalculator,
    rasiPalanRepository: RasiPalanRepository,
    jathagamRepository: JathagamRepository,
    babyNamesRepository: BabyNamesRepository
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val userPrefs by preferencesRepository.preferences.collectAsState()
    val currentLanguage = userPrefs.language

    // Instantiate ViewModels
    val panchangamViewModel = remember { PanchangamViewModel(preferencesRepository, panchangamCalculator) }
    val jathagamViewModel = remember { JathagamViewModel(jathagamRepository, preferencesRepository) }
    val rasiPalanViewModel = remember { RasiPalanViewModel(jathagamRepository, preferencesRepository, rasiPalanRepository) }
    val matchMakingViewModel = remember { MatchMakingViewModel() }
    val babyNamesViewModel = remember { BabyNamesViewModel(babyNamesRepository) }
    val settingsViewModel = remember { SettingsViewModel(preferencesRepository) }

    val isTopLevelRoute = currentRoute in listOf(
        Screen.Panchangam.route,
        Screen.Jathagam.route,
        Screen.RasiPalan.route,
        Screen.WeddingMatch.route,
        Screen.BabyNames.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isTopLevelRoute) {
                TempleTopBar(
                    currentLanguage = currentLanguage,
                    onToggleLanguage = {
                        val nextLang = when (currentLanguage) {
                            AppLanguage.TAMIL -> AppLanguage.HINDI
                            AppLanguage.HINDI -> AppLanguage.ENGLISH
                            AppLanguage.ENGLISH -> AppLanguage.TAMIL
                        }
                        preferencesRepository.setLanguage(nextLang)
                    },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
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
                            popUpTo(Screen.Panchangam.route) {
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
            startDestination = Screen.Panchangam.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Panchangam Screen (Primary)
            composable(Screen.Panchangam.route) {
                PanchangamScreen(viewModel = panchangamViewModel)
            }

            // 2. Jathagam (Horoscope) Screen
            composable(Screen.Jathagam.route) {
                JathagamScreen(
                    viewModel = jathagamViewModel,
                    onNavigateToRasiPalanWithTimeframe = { timeframe ->
                        val currentHoroscope = jathagamViewModel.uiState.value.horoscopeResult
                        if (currentHoroscope != null) {
                            rasiPalanViewModel.selectRasi(currentHoroscope.chandraRasi)
                        }
                        rasiPalanViewModel.selectTimeframe(timeframe)
                        navController.navigate(Screen.RasiPalan.route)
                    }
                )
            }

            // 3. Rasi Palan Screen
            composable(Screen.RasiPalan.route) {
                RasiPalanScreen(
                    viewModel = rasiPalanViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToJathagam = {
                        navController.navigate(Screen.Jathagam.route) {
                            popUpTo(Screen.Panchangam.route)
                            launchSingleTop = true
                        }
                    }
                )
            }

            // 4. Wedding Match (திருமணப் பொருத்தம் & செவ்வாய் தோஷம்) Screen
            composable(Screen.WeddingMatch.route) {
                MatchMakingScreen(
                    viewModel = matchMakingViewModel,
                    currentLanguage = currentLanguage
                )
            }

            // 5. Baby Names by Nakshatram Screen
            composable(Screen.BabyNames.route) {
                BabyNamesScreen(
                    viewModel = babyNamesViewModel,
                    currentLanguage = currentLanguage
                )
            }

            // 6. Settings Screen
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

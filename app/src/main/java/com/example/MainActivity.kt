package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.local.AppDatabase
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.AppLanguage
import com.example.data.repository.DharmaSastraRepository
import com.example.data.repository.FestivalRepository
import com.example.data.repository.RasiPalanRepository
import com.example.data.repository.TempleRepository
import com.example.data.service.AstrologyCalculator
import com.example.data.service.PrecisionLahiriAstrologyCalculator
import com.example.data.service.StandardPanchangamCalculator
import com.example.ui.components.TempleBottomBar
import com.example.ui.components.TempleTopBar
import com.example.ui.navigation.Screen
import com.example.ui.screens.calendar.CalendarScreen
import com.example.ui.screens.calendar.CalendarViewModel
import com.example.ui.screens.calendar.FestivalDetailScreen
import com.example.ui.screens.dharmasastra.DharmaSastraDetailScreen
import com.example.ui.screens.dharmasastra.DharmaSastraScreen
import com.example.ui.screens.dharmasastra.DharmaSastraViewModel
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.home.HomeViewModel
import com.example.ui.screens.jathagam.JathagamScreen
import com.example.ui.screens.jathagam.JathagamViewModel
import com.example.ui.screens.panchangam.PanchangamScreen
import com.example.ui.screens.panchangam.PanchangamViewModel
import com.example.ui.screens.rasipalan.RasiPalanScreen
import com.example.ui.screens.rasipalan.RasiPalanViewModel
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.settings.SettingsViewModel
import com.example.ui.screens.temple.TempleScreen
import com.example.ui.screens.temple.TempleViewModel
import com.example.ui.theme.SriSivaKovilTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(this)
        val preferencesRepository = UserPreferencesRepository(this)
        val panchangamCalculator = StandardPanchangamCalculator()
        val astrologyCalculator: AstrologyCalculator = PrecisionLahiriAstrologyCalculator()
        val festivalRepository = FestivalRepository()
        val templeRepository = TempleRepository(database.templeEventDao())
        val rasiPalanRepository = RasiPalanRepository()
        val dharmaSastraRepository = DharmaSastraRepository()
        val jathagamRepository = com.example.data.repository.JathagamRepository(database.horoscopeDao(), astrologyCalculator)

        setContent {
            val userPrefs by preferencesRepository.preferences.collectAsState()

            SriSivaKovilTheme(
                darkTheme = if (userPrefs.useSystemTheme) androidx.compose.foundation.isSystemInDarkTheme() else userPrefs.isDarkMode
            ) {
                MainAppContainer(
                    preferencesRepository = preferencesRepository,
                    database = database,
                    panchangamCalculator = panchangamCalculator,
                    astrologyCalculator = astrologyCalculator,
                    festivalRepository = festivalRepository,
                    templeRepository = templeRepository,
                    rasiPalanRepository = rasiPalanRepository,
                    dharmaSastraRepository = dharmaSastraRepository,
                    jathagamRepository = jathagamRepository
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
    festivalRepository: FestivalRepository,
    templeRepository: TempleRepository,
    rasiPalanRepository: RasiPalanRepository,
    dharmaSastraRepository: DharmaSastraRepository,
    jathagamRepository: com.example.data.repository.JathagamRepository
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val userPrefs by preferencesRepository.preferences.collectAsState()
    val currentLanguage = userPrefs.language

    // Instantiate ViewModels
    val homeViewModel = remember { HomeViewModel(preferencesRepository, panchangamCalculator, festivalRepository) }
    val calendarViewModel = remember { CalendarViewModel(preferencesRepository, panchangamCalculator, festivalRepository) }
    val panchangamViewModel = remember { PanchangamViewModel(preferencesRepository, panchangamCalculator) }
    val jathagamViewModel = remember { JathagamViewModel(jathagamRepository, preferencesRepository) }
    val templeViewModel = remember { TempleViewModel(templeRepository, preferencesRepository) }
    val rasiPalanViewModel = remember { RasiPalanViewModel(jathagamRepository, preferencesRepository, rasiPalanRepository) }
    val dharmaSastraViewModel = remember { DharmaSastraViewModel(preferencesRepository, dharmaSastraRepository) }
    val settingsViewModel = remember { SettingsViewModel(preferencesRepository) }

    val isTopLevelRoute = currentRoute in listOf(
        Screen.Home.route,
        Screen.Calendar.route,
        Screen.Panchangam.route,
        Screen.Jathagam.route,
        Screen.Temple.route
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
                    onNavigateToRasiPalan = { navController.navigate(Screen.RasiPalan.route) },
                    onNavigateToDharmaSastra = { navController.navigate(Screen.DharmaSastra.route) },
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
                            popUpTo(Screen.Home.route) {
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
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Home Screen
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToCalendar = { navController.navigate(Screen.Calendar.route) },
                    onNavigateToPanchangam = { navController.navigate(Screen.Panchangam.route) },
                    onNavigateToJathagam = { navController.navigate(Screen.Jathagam.route) },
                    onNavigateToTemple = { navController.navigate(Screen.Temple.route) },
                    onNavigateToRasiPalan = { navController.navigate(Screen.RasiPalan.route) },
                    onNavigateToDharmaSastra = { navController.navigate(Screen.DharmaSastra.route) },
                    onNavigateToFestivalDetail = { festId ->
                        navController.navigate(Screen.FestivalDetail.createRoute(festId))
                    }
                )
            }

            // 2. Tamil Calendar Screen
            composable(Screen.Calendar.route) {
                CalendarScreen(
                    viewModel = calendarViewModel,
                    onNavigateToPanchangamDate = { date ->
                        panchangamViewModel.setDate(date)
                        navController.navigate(Screen.Panchangam.route)
                    },
                    onNavigateToFestivalDetail = { festId ->
                        navController.navigate(Screen.FestivalDetail.createRoute(festId))
                    }
                )
            }

            // 3. Panchangam Screen
            composable(Screen.Panchangam.route) {
                PanchangamScreen(viewModel = panchangamViewModel)
            }

            // 4. Jathagam (Horoscope) Screen
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

            // 5. Temple Screen
            composable(Screen.Temple.route) {
                TempleScreen(viewModel = templeViewModel)
            }

            // 6. Rasi Palan Screen
            composable(Screen.RasiPalan.route) {
                RasiPalanScreen(
                    viewModel = rasiPalanViewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToJathagam = {
                        navController.navigate(Screen.Jathagam.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
                )
            }


            // 7. Dharma Sastra Screen
            composable(Screen.DharmaSastra.route) {
                DharmaSastraScreen(
                    viewModel = dharmaSastraViewModel,
                    onTopicClick = { topicId ->
                        navController.navigate(Screen.DharmaSastraDetail.createRoute(topicId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // 8. Festival Detail Screen
            composable(
                route = Screen.FestivalDetail.route,
                arguments = listOf(navArgument("festivalId") { type = NavType.StringType })
            ) { backStackEntry ->
                val festivalId = backStackEntry.arguments?.getString("festivalId") ?: ""
                FestivalDetailScreen(
                    festivalId = festivalId,
                    festivalRepository = festivalRepository,
                    currentLanguage = currentLanguage,
                    onBack = { navController.popBackStack() }
                )
            }

            // 9. Dharma Sastra Detail Screen
            composable(
                route = Screen.DharmaSastraDetail.route,
                arguments = listOf(navArgument("topicId") { type = NavType.StringType })
            ) { backStackEntry ->
                val topicId = backStackEntry.arguments?.getString("topicId") ?: ""
                DharmaSastraDetailScreen(
                    topicId = topicId,
                    repository = dharmaSastraRepository,
                    currentLanguage = currentLanguage,
                    onBack = { navController.popBackStack() }
                )
            }

            // 10. Settings Screen
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Calendar : Screen("calendar")
    object Panchangam : Screen("panchangam")
    object Jathagam : Screen("jathagam")
    object Temple : Screen("temple")
    object RasiPalan : Screen("rasi_palan")
    object DharmaSastra : Screen("dharma_sastra")
    object Settings : Screen("settings")
    object FestivalDetail : Screen("festival_detail/{festivalId}") {
        fun createRoute(festivalId: String) = "festival_detail/$festivalId"
    }
    object DharmaSastraDetail : Screen("dharma_sastra_detail/{topicId}") {
        fun createRoute(topicId: String) = "dharma_sastra_detail/$topicId"
    }
}

package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Panchangam : Screen("panchangam")
    object Jathagam : Screen("jathagam")
    object WeddingMatch : Screen("wedding_match")
    object BabyNames : Screen("baby_names")
    object Settings : Screen("settings")
}

package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.repository.AppStrings
import com.example.ui.navigation.Screen
import com.example.ui.theme.TempleGold
import com.example.ui.theme.TempleGoldLight
import com.example.ui.theme.TempleMaroon
import com.example.ui.theme.TempleSandal

data class NavItem(
    val route: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val labelProvider: (AppLanguage) -> String,
    val testTag: String
)

@Composable
fun TempleBottomBar(
    currentRoute: String?,
    currentLanguage: AppLanguage,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        NavItem(
            route = Screen.Home.route,
            filledIcon = Icons.Filled.Home,
            outlinedIcon = Icons.Outlined.Home,
            labelProvider = { AppStrings.home(it) },
            testTag = "nav_bottom_home"
        ),
        NavItem(
            route = Screen.Calendar.route,
            filledIcon = Icons.Filled.CalendarMonth,
            outlinedIcon = Icons.Outlined.CalendarMonth,
            labelProvider = { AppStrings.calendar(it) },
            testTag = "nav_bottom_calendar"
        ),
        NavItem(
            route = Screen.Panchangam.route,
            filledIcon = Icons.Filled.WbSunny,
            outlinedIcon = Icons.Outlined.WbSunny,
            labelProvider = { AppStrings.panchangam(it) },
            testTag = "nav_bottom_panchangam"
        ),
        NavItem(
            route = Screen.Jathagam.route,
            filledIcon = Icons.Filled.AutoAwesome,
            outlinedIcon = Icons.Outlined.AutoAwesome,
            labelProvider = { AppStrings.jathagam(it) },
            testTag = "nav_bottom_jathagam"
        ),
        NavItem(
            route = Screen.RasiPalan.route,
            filledIcon = Icons.Filled.Star,
            outlinedIcon = Icons.Outlined.Star,
            labelProvider = { AppStrings.rasiPalan(it) },
            testTag = "nav_bottom_rasi_palan"
        )
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val label = item.labelProvider(currentLanguage)

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}

package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.repository.AppStrings
import com.example.ui.navigation.Screen

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
            route = Screen.Jathagam.route,
            filledIcon = Icons.Filled.AutoAwesome,
            outlinedIcon = Icons.Outlined.AutoAwesome,
            labelProvider = { AppStrings.jathagam(it) },
            testTag = "nav_bottom_jathagam"
        ),
        NavItem(
            route = Screen.WeddingMatch.route,
            filledIcon = Icons.Filled.Favorite,
            outlinedIcon = Icons.Outlined.FavoriteBorder,
            labelProvider = { AppStrings.weddingMatch(it) },
            testTag = "nav_bottom_wedding_match"
        ),
        NavItem(
            route = Screen.BabyNames.route,
            filledIcon = Icons.Filled.ChildCare,
            outlinedIcon = Icons.Outlined.ChildCare,
            labelProvider = { AppStrings.babyNames(it) },
            testTag = "nav_bottom_baby_names"
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
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        maxLines = 1,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}

package com.example.ui.components

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppLanguage
import com.example.data.service.GpsLocationHelper
import com.example.ui.theme.TempleGold
import com.example.ui.theme.TempleMaroon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

data class LocationSearchResult(
    val title: String,
    val subtitle: String,
    val latitude: Double,
    val longitude: Double,
    val formattedString: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchDialog(
    currentLanguage: AppLanguage,
    initialPlace: String,
    onDismiss: () -> Unit,
    onSelectLocation: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val gpsHelper = remember { GpsLocationHelper(context) }

    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf<List<LocationSearchResult>>(emptyList()) }
    var isGpsLoading by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Search / Quick Picks, 1: Custom Coordinates

    var customLat by remember { mutableStateOf("") }
    var customLon by remember { mutableStateOf("") }
    var customPlaceName by remember { mutableStateOf("") }

    val quickPicksFiji = listOf(
        "Nadi, Fiji" to (-17.80 to 177.41),
        "Suva, Fiji" to (-18.14 to 178.44),
        "Lautoka, Fiji" to (-17.61 to 177.45),
        "Labasa, Fiji" to (-16.43 to 179.37),
        "Ba, Fiji" to (-17.53 to 177.67),
        "Sigatoka, Fiji" to (-18.14 to 177.51)
    )

    val quickPicksTamilNadu = listOf(
        "Chennai, Tamil Nadu" to (13.08 to 80.27),
        "Madurai, Tamil Nadu" to (9.93 to 78.12),
        "Coimbatore, Tamil Nadu" to (11.01 to 76.95),
        "Tiruchirappalli, Tamil Nadu" to (10.79 to 78.70),
        "Salem, Tamil Nadu" to (11.66 to 78.14),
        "Tirunelveli, Tamil Nadu" to (8.71 to 77.75),
        "Thanjavur, Tamil Nadu" to (10.78 to 79.13),
        "Erode, Tamil Nadu" to (11.34 to 77.71),
        "Vellore, Tamil Nadu" to (12.91 to 79.13),
        "Tirupur, Tamil Nadu" to (11.10 to 77.34),
        "Kanchipuram, Tamil Nadu" to (12.83 to 79.70),
        "Dindigul, Tamil Nadu" to (10.36 to 77.98),
        "Nagercoil, Tamil Nadu" to (8.18 to 77.43)
    )

    val quickPicksGlobal = listOf(
        "Bengaluru, India" to (12.97 to 77.59),
        "Mumbai, India" to (19.07 to 72.87),
        "New Delhi, India" to (28.61 to 77.20),
        "Singapore" to (1.35 to 103.82),
        "Kuala Lumpur, Malaysia" to (3.14 to 101.69),
        "Sydney, Australia" to (-33.87 to 151.21),
        "Melbourne, Australia" to (-37.81 to 144.96),
        "Auckland, New Zealand" to (-36.85 to 174.76),
        "London, UK" to (51.51 to -0.13),
        "Dubai, UAE" to (25.20 to 55.27),
        "Toronto, Canada" to (43.65 to -79.38),
        "New York, USA" to (40.71 to -74.00),
        "San Francisco, USA" to (37.77 to -122.41)
    )

    fun searchLocationWithGeocoder(query: String) {
        if (query.isBlank()) {
            searchResults = emptyList()
            return
        }
        isSearching = true
        scope.launch {
            try {
                val results = withContext(Dispatchers.IO) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    @Suppress("DEPRECATION")
                    val list = geocoder.getFromLocationName(query, 6) ?: emptyList<Address>()
                    list.map { addr ->
                        val cityName = addr.locality ?: addr.subAdminArea ?: addr.adminArea ?: query
                        val country = addr.countryName ?: ""
                        val title = if (country.isNotBlank()) "$cityName, $country" else cityName
                        val subtitle = "Lat: ${String.format(Locale.US, "%.4f", addr.latitude)}°, Lon: ${String.format(Locale.US, "%.4f", addr.longitude)}°"
                        val formatted = "$title (GPS: ${String.format(Locale.US, "%.2f", addr.latitude)}, ${String.format(Locale.US, "%.2f", addr.longitude)})"
                        LocationSearchResult(
                            title = title,
                            subtitle = subtitle,
                            latitude = addr.latitude,
                            longitude = addr.longitude,
                            formattedString = formatted
                        )
                    }
                }
                searchResults = results
            } catch (e: Exception) {
                searchResults = emptyList()
            } finally {
                isSearching = false
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f)
                .testTag("location_search_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = TempleMaroon.copy(alpha = 0.12f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TempleMaroon, modifier = Modifier.size(20.dp))
                            }
                        }
                        Column {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.TAMIL -> "பிறந்த இடம் தேர்வு & தேடல்"
                                    AppLanguage.HINDI -> "जन्म स्थान खोजें एवं चुनें"
                                    AppLanguage.ENGLISH -> "Search & Select Birth Place"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TempleMaroon
                            )
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.TAMIL -> "வரைபடம் / ஊர் பெயர் / ஜிபிஎஸ் மூலம்"
                                    AppLanguage.HINDI -> "गूगल मैप्स / स्थान नाम / जीपीएस द्वारा"
                                    AppLanguage.ENGLISH -> "Via Google Maps / City Name / GPS"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // GPS Button Action Card
                Surface(
                    onClick = {
                        if (gpsHelper.hasLocationPermission()) {
                            isGpsLoading = true
                            scope.launch {
                                val loc = gpsHelper.getCurrentLocation()
                                isGpsLoading = false
                                if (loc != null) {
                                    val formatted = "${loc.locationName} (GPS: ${String.format(Locale.US, "%.2f", loc.latitude)}, ${String.format(Locale.US, "%.2f", loc.longitude)})"
                                    onSelectLocation(formatted)
                                    onDismiss()
                                } else {
                                    Toast.makeText(context, "GPS location unavailable", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Location permission required", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = TempleGold.copy(alpha = 0.15f),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(TempleGold)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (isGpsLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = TempleMaroon)
                        } else {
                            Icon(Icons.Filled.MyLocation, contentDescription = null, tint = TempleMaroon)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.TAMIL -> "🎯 எனது தற்போதைய இடத்தை தானாகக் கண்டறி (GPS)"
                                    AppLanguage.HINDI -> "🎯 मेरा वर्तमान स्थान उपयोग करें (GPS)"
                                    AppLanguage.ENGLISH -> "🎯 Use My Current Live Location (GPS)"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TempleMaroon
                            )
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.TAMIL -> "தொலைபேசி ஜிபிஎஸ் மூலம் துல்லிய அட்ச/தீர்க்கரேகை"
                                    AppLanguage.HINDI -> "सटीक अक्षांश एवं देशांतर प्राप्त करें"
                                    AppLanguage.ENGLISH -> "Automatic high-precision latitude & longitude"
                                },
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Input Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        searchLocationWithGeocoder(it)
                    },
                    placeholder = {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.TAMIL -> "ஊர் அல்லது நாட்டின் பெயரைத் தட்டச்சு செய்யவும்... (எ.கா: Nadi, Chennai)"
                                AppLanguage.HINDI -> "शहर या स्थान का नाम लिखें... (उदा: Nadi, Chennai)"
                                AppLanguage.ENGLISH -> "Search city, town or country... (e.g. Nadi, Chennai, London)"
                            },
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    leadingIcon = {
                        if (isSearching) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = TempleMaroon)
                        } else {
                            Icon(Icons.Filled.Search, contentDescription = null, tint = TempleMaroon)
                        }
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                                searchResults = emptyList()
                            }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Search Results List (if query active)
                if (searchResults.isNotEmpty()) {
                    Text(
                        text = when (currentLanguage) {
                            AppLanguage.TAMIL -> "தேடல் முடிவுகள் (Google Maps / Geocoder):"
                            AppLanguage.HINDI -> "खोज परिणाम:"
                            AppLanguage.ENGLISH -> "Search Results:"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = TempleMaroon
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(searchResults) { item ->
                            Surface(
                                onClick = {
                                    onSelectLocation(item.formattedString)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(Icons.Filled.Place, contentDescription = null, tint = TempleMaroon)
                                    Column {
                                        Text(item.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(item.subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Quick Pick Tabs
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = TempleMaroon,
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("🇫🇯 Fiji", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("🇮🇳 Tamil Nadu", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = { Text("🌐 Global", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val activeList = when (selectedTab) {
                        0 -> quickPicksFiji
                        1 -> quickPicksTamilNadu
                        else -> quickPicksGlobal
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(activeList) { (placeName, coords) ->
                            val isSelected = initialPlace.contains(placeName, ignoreCase = true)
                            Surface(
                                onClick = {
                                    onSelectLocation(placeName)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) TempleGold.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(TempleMaroon)) else null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.LocationCity,
                                            contentDescription = null,
                                            tint = if (isSelected) TempleMaroon else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = placeName,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 13.sp,
                                            color = if (isSelected) TempleMaroon else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Text(
                                        text = "${coords.first}°, ${coords.second}°",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Custom Manual Location Input & Apply
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(when (currentLanguage) { AppLanguage.TAMIL -> "ரத்து"; AppLanguage.HINDI -> "रद्द करें"; AppLanguage.ENGLISH -> "Cancel" })
                    }

                    if (searchQuery.isNotBlank()) {
                        Button(
                            onClick = {
                                onSelectLocation(searchQuery)
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TempleMaroon)
                        ) {
                            Text(when (currentLanguage) { AppLanguage.TAMIL -> "இதை தேர்வு செய்"; AppLanguage.HINDI -> "चुनें"; AppLanguage.ENGLISH -> "Select Typed" }, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

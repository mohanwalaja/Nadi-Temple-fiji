package com.example.ui.screens.temple

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.TempleContact
import com.example.data.model.TempleEvent
import com.example.data.repository.AppStrings
import com.example.ui.theme.*
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter

@Composable
fun TempleScreen(
    viewModel: TempleViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val lang = state.language
    val context = LocalContext.current
    var showPhotoViewer by remember { mutableStateOf(false) }

    if (showPhotoViewer) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showPhotoViewer = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .testTag("dialog_temple_photo"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = TempleMaroonDark),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, TempleGold)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (lang == AppLanguage.TAMIL) "அசல் திருக்கோயில் காட்சி" else "Original Temple Photo (Nadi, Fiji)",
                            color = TempleGoldLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        IconButton(
                            onClick = { showPhotoViewer = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Image(
                        painter = painterResource(id = R.drawable.img_temple_gopuram),
                        contentDescription = "Sri Siva Subramaniya Swami Temple Nadi Fiji",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (lang == AppLanguage.TAMIL)
                            "தென் அரைக்கோளத்தின் மிகப்பெரிய திராவிடக் கலைக் கோயில் — ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில், நாடி, பிஜி தீவுகள்"
                        else
                            "Largest South Indian Dravidian Hindu temple in the Southern Hemisphere — Sri Siva Subramaniya Swami Kovil, Nadi, Fiji Islands",
                        color = Color.White,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("temple_screen_container"),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Temple Banner with Original Photo Showcase
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clickable { showPhotoViewer = true }
                    .testTag("temple_hero_banner")
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_temple_gopuram),
                    contentDescription = "Sri Siva Subramaniya Swami Kovil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    TempleMaroon.copy(alpha = 0.6f),
                                    TempleMaroonDark.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Top right "Original Photo" badge
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, TempleGold),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ZoomIn, contentDescription = null, tint = TempleGoldLight, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (lang == AppLanguage.TAMIL) "அசல் புகைப்படம்" else "Original Photo",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TempleGoldLight
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (lang == AppLanguage.TAMIL) "ஸ்ரீ சிவ சுப்பிரமணிய சுவாமி திருக்கோயில்" else "Sri Siva Subramaniya Swami Kovil",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = if (lang == AppLanguage.TAMIL) "மூலவர்: வள்ளி தெய்வானை சமேத ஸ்ரீ சுப்பிரமணியர் (நாடி, பிஜி)" else "Presiding Deity: Lord Murugan with Valli & Deivayanai (Nadi, Fiji)",
                        fontSize = 12.sp,
                        color = TempleGoldLight
                    )
                }
            }
        }

        // 2. Temple Opening Status & Today's Schedule Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (state.isTempleOpenNow) SacredGreen else TempleKumkum)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (state.isTempleOpenNow) AppStrings.templeStatusOpen(lang) else AppStrings.templeStatusClosed(lang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (state.isTempleOpenNow) SacredGreen else TempleKumkum
                            )
                        }

                        Text(
                            text = if (lang == AppLanguage.TAMIL) "இன்று நடை சாத்துதல்: ${state.todayClosingTime}" else "Closes: ${state.todayClosingTime}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                    // Daily Opening Rule Note
                    Text(
                        text = if (lang == AppLanguage.TAMIL)
                            "• நாள்தோறும் விடியற்காலை 6:00 மணிக்கு திருக்கோயில் நடை திறக்கப்படும்.\n• திங்கள், செவ்வாய், வெள்ளி: இரவு 8:00 மணி வரை\n• புதன், வியாழன், சனி, ஞாயிறு: இரவு 7:00 மணி வரை"
                        else
                            "• Temple opens daily at 6:00 AM.\n• Mon, Tue, Fri: Closes at 8:00 PM\n• Wed, Thu, Sat, Sun: Closes at 7:00 PM",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                    )
                }
            }
        }

        // 3. Daily Arti Timings Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (lang == AppLanguage.TAMIL) "தினசரி நித்திய ஆரத்தி நேரங்கள்" else "Daily Regular Arti Timings",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // Morning Arti
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                color = TempleGold.copy(alpha = 0.3f),
                                shape = CircleShape,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(imageVector = Icons.Default.WbSunny, contentDescription = null, tint = TempleGoldDark, modifier = Modifier.size(22.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = AppStrings.morningArti(lang), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = state.schedule.morningArtiTime, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TempleMaroon)
                        }

                        // Evening Arti
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                color = TempleSaffron.copy(alpha = 0.2f),
                                shape = CircleShape,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(imageVector = Icons.Default.NightlightRound, contentDescription = null, tint = TempleSaffron, modifier = Modifier.size(22.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = AppStrings.eveningArti(lang), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = state.schedule.eveningArtiTime, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TempleMaroon)
                        }
                    }
                }
            }
        }

        // 4. Temple Contacts (Manager & Head Priest) with Dial intent
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = if (lang == AppLanguage.TAMIL) "திருக்கோயில் தொடர்புகள்" else "Temple Contacts",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TempleMaroon,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                state.contacts.forEach { contact ->
                    ContactCard(
                        contact = contact,
                        lang = lang,
                        onCall = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${contact.phoneNumber}")
                            }
                            context.startActivity(intent)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // 5. Special Poojas & Temple Events List
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = if (lang == AppLanguage.TAMIL) "திருக்கோயில் சிறப்பு பூஜைகள் & திருவிழாக்கள் (${state.events.size})" else "Special Poojas & Temple Events (${state.events.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TempleMaroon,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }

        items(state.events) { event ->
            val isSaved = state.savedEventIds.contains(event.id)
            TempleEventCard(
                event = event,
                isSaved = isSaved,
                lang = lang,
                onToggleSaved = { viewModel.toggleEventReminder(event.id) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun ContactCard(
    contact: TempleContact,
    lang: AppLanguage,
    onCall: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCall() }
            .testTag("contact_card_${contact.phoneNumber}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = TempleMaroon.copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = TempleMaroon, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = if (lang == AppLanguage.TAMIL) contact.roleTa else contact.roleEn,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (lang == AppLanguage.TAMIL) contact.nameTa else contact.nameEn,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = contact.phoneNumber,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TempleMaroon
                    )
                }
            }

            FilledTonalButton(
                onClick = onCall,
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = TempleMaroon, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.testTag("btn_call_${contact.phoneNumber}")
            ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = AppStrings.call(lang), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun TempleEventCard(
    event: TempleEvent,
    isSaved: Boolean,
    lang: AppLanguage,
    onToggleSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("temple_event_${event.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    val dateFormatted = event.date.format(DateTimeFormatter.ofPattern("EEEE, d MMM"))
                    Text(
                        text = "$dateFormatted • ${event.startTime} - ${event.endTime}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                IconButton(
                    onClick = onToggleSaved,
                    modifier = Modifier.size(32.dp).testTag("btn_remind_${event.id}")
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                        contentDescription = "Toggle Reminder",
                        tint = if (isSaved) TempleGoldDark else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.getPoojaName(lang),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TempleMaroon
            )

            Text(
                text = "தெய்வம்: ${event.getDeity(lang)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = event.getDescription(lang),
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
            )
        }
    }
}

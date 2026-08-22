package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Graha
import com.example.data.model.PlanetPosition
import com.example.data.model.Rasi
import com.example.ui.theme.*

@Composable
fun SouthIndianRasiChart(
    lagnaRasi: Rasi,
    planetPositions: List<PlanetPosition>,
    lang: AppLanguage,
    chartTitle: String = if (lang == AppLanguage.TAMIL) "ராசி சக்கரம் (Rasi Chart)" else "Natal Rasi Chart",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = chartTitle,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 4x4 Grid layout for South Indian Chart
        // Row 0: Meenam (12), Mesham (1), Rishabam (2), Mithunam (3)
        // Row 1: Kumbam (11), [Center], Kadagam (4)
        // Row 2: Magaram (10), [Center], Simham (5)
        // Row 3: Dhanusu (9), Viruchigam (8), Thulam (7), Kanni (6)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            // Row 0
            Row(modifier = Modifier.weight(1f)) {
                ChartCell(Rasi.MEENAM, lagnaRasi, planetPositions, lang, Modifier.weight(1f))
                ChartCell(Rasi.MESHAM, lagnaRasi, planetPositions, lang, Modifier.weight(1f))
                ChartCell(Rasi.RISHABAM, lagnaRasi, planetPositions, lang, Modifier.weight(1f))
                ChartCell(Rasi.MITHUNAM, lagnaRasi, planetPositions, lang, Modifier.weight(1f))
            }
            // Row 1
            Row(modifier = Modifier.weight(1f)) {
                ChartCell(Rasi.KUMBAM, lagnaRasi, planetPositions, lang, Modifier.weight(1f))
                CenterCell(lagnaRasi, lang, Modifier.weight(2f))
                ChartCell(Rasi.KADAGAM, lagnaRasi, planetPositions, lang, Modifier.weight(1f))
            }
            // Row 2
            Row(modifier = Modifier.weight(1f)) {
                ChartCell(Rasi.MAGARAM, lagnaRasi, planetPositions, lang, Modifier.weight(1f))
                CenterCellBottom(lang, Modifier.weight(2f))
                ChartCell(Rasi.SIMHAM, lagnaRasi, planetPositions, lang, Modifier.weight(1f))
            }
            // Row 3
            Row(modifier = Modifier.weight(1f)) {
                ChartCell(Rasi.DHANUSU, lagnaRasi, planetPositions, lang, Modifier.weight(1f))
                ChartCell(Rasi.VIRUCHIGAM, lagnaRasi, planetPositions, lang, Modifier.weight(1f))
                ChartCell(Rasi.THULAM, lagnaRasi, planetPositions, lang, Modifier.weight(1f))
                ChartCell(Rasi.KANNI, lagnaRasi, planetPositions, lang, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ChartCell(
    rasi: Rasi,
    lagnaRasi: Rasi,
    planets: List<PlanetPosition>,
    lang: AppLanguage,
    modifier: Modifier = Modifier
) {
    val isLagna = rasi == lagnaRasi
    val planetsInRasi = planets.filter { it.rasi == rasi }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .border(0.5.dp, MaterialTheme.colorScheme.outline)
            .background(if (isLagna) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
            .padding(3.dp)
    ) {
        // Rasi Name
        Text(
            text = if (lang == AppLanguage.TAMIL) rasi.nameTa else rasi.nameEn.take(4),
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.TopStart)
        )

        if (isLagna) {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(3.dp),
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = if (lang == AppLanguage.TAMIL) "லக்" else "LAG",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                )
            }
        }

        // Planets inside this box
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            planetsInRasi.forEach { p ->
                val planetText = if (lang == AppLanguage.TAMIL) p.graha.shortTa else p.graha.shortEn
                val suffix = if (p.isRetrograde) "(R)" else ""
                Text(
                    text = "$planetText$suffix",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun CenterCell(lagna: Rasi, lang: AppLanguage, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(0.5.dp, MaterialTheme.colorScheme.outline)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "ஸ்ரீ சிவ சுப்பிரமணியர்",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "லக்னம்: ${if (lang == AppLanguage.TAMIL) lagna.nameTa else lagna.nameEn}",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun CenterCellBottom(lang: AppLanguage, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(0.5.dp, MaterialTheme.colorScheme.outline)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (lang == AppLanguage.TAMIL) "திருவருள் துணை ॐ" else "Divine Blessings ॐ",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

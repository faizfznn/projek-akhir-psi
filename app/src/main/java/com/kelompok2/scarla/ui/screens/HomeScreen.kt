package com.kelompok2.scarla.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.kelompok2.scarla.R
import com.kelompok2.scarla.ui.theme.*
import kotlin.math.absoluteValue
import com.kelompok2.scarla.ui.components.AppButton
import com.kelompok2.scarla.ui.components.ButtonType

// --- DATA MODELS ---
data class StreakDay(
    val dayName: String,
    val isActive: Boolean
)

data class BeaItem(
    val title: String,
    val subtitle: String,
    val imageRes: Int
)

data class StudyModule(
    val title: String,
    val imageRes: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

    val streakDays = listOf(
        StreakDay("Sen", true),
        StreakDay("Sel", false),
        StreakDay("Rab", false),
        StreakDay("Kam", false),
        StreakDay("Jum", false),
        StreakDay("Sab", false),
        StreakDay("Min", false)
    )

    val studyModules = listOf(
        StudyModule("Belajar Dasar HTML", R.drawable.ic_html),
        StudyModule("Belajar Dasar HTML", R.drawable.ic_html)
    )

    val scholarshipList = listOf(
        BeaItem(
            title = "Glow & Lovely Bintang Beasiswa",
            subtitle = "Bantuan edukasi bagi perempuan Indonesia untuk melanjutkan pendidikan ke jenjang tinggi",
            imageRes = R.drawable.bea_1
        ),
        BeaItem(
            title = "Beasiswa SEMESTA",
            subtitle = "Jenjang pendidikan S1 & S2 di Perguruan Tinggi dengan sistem Pendidikan Jarak Jauh (PJJ)",
            imageRes = R.drawable.bea_2
        ),
        BeaItem(
            title = "Tanoto Foundation",
            subtitle = "Program kepemimpinan dan beasiswa terstruktur untuk mencetak pemimpin masa depan",
            imageRes = R.drawable.bea_3
        )
    )

    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { scholarshipList.size }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFCFC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(bottom = 80.dp)
        ) {
            // Header Logo
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.font_logo),
                    contentDescription = "Logo SCARLA",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(160.dp)
                        .height(40.dp)
                )
            }

            // Wrapper Konten
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {

                // --- 1. Bagian Hello & Notifikasi ---
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Hello, Ahmad",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontWeight = FontWeight(700),
                            color = Neutral900
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_notifications_none_24),
                        contentDescription = "Notifications",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // --- 2. Container Streak Mingguan ---
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    streakDays.forEach { item ->
                        StreakItem(day = item)
                    }
                }

                // --- 3. Bagian Lanjut Belajar ---
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Lanjut Belajar",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontWeight = FontWeight(700),
                            color = Neutral900
                        )
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        studyModules.forEach { module ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.width(135.dp)
                            ) {
                                // Card Modul Belajar
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .shadow(elevation = 3.dp, shape = RoundedCornerShape(16.dp))
                                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                                        .padding(12.dp)
                                        .size(115.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = module.imageRes),
                                        contentDescription = module.title,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Text(
                                        text = module.title,
                                        style = TextStyle(
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                            fontWeight = FontWeight(700),
                                            color = Color(0xFF333333)
                                        ),
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                // Tombol Kapsul Continue
                                AppButton(
                                    text = "Continue",
                                    buttonType = ButtonType.PRIMARY,
                                    onClick = { /* Navigasi */ },
                                    modifier = Modifier
                                        .width(115.dp)
                                        .height(32.dp)
                                )
                            }
                        }

                        // Tombol Bulat Continue untuk geser ke kanan
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .width(70.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(Color(0xFFFFD100), shape = CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Continue",
                                style = TextStyle(
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                    fontWeight = FontWeight(500),
                                    color = Color(0xFF555555)
                                )
                            )
                        }
                    }
                }

                // --- 4. Bagian Informasi Beasiswa (Carousel) ---
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Informasi Beasiswa",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontWeight = FontWeight(700),
                            color = Neutral900
                        )
                    )

                    HorizontalPager(
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 96.dp),
                        pageSpacing = 12.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                    ) { page ->
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                ).absoluteValue

                        val scale = lerp(
                            start = 0.82f,
                            stop = 1.0f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        val alpha = lerp(
                            start = 0.5f,
                            stop = 1.0f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        val shadowElevation = lerp(
                            start = 0f,
                            stop = 6f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        BeasiswaCard(
                            item = scholarshipList[page],
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    this.alpha = alpha
                                    // Memperbaiki properti kedalaman visual di GraphicsLayerScope
                                    this.shadowElevation = shadowElevation
                                }
                                .shadow(
                                    elevation = shadowElevation.dp,
                                    shape = RoundedCornerShape(24.dp),
                                    clip = false
                                )
                        )
                    }
                }
            }
        }
    }
}

// Card Beasiswa (Fokus Layout Mirip Mockup)
@Composable
fun BeasiswaCard(
    item: BeaItem,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(215.dp)
            .height(300.dp)
            .background(color = Color.White, shape = RoundedCornerShape(size = 24.dp))
            .padding(10.dp)
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(155.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Text(
                text = item.title,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF222222),
                    textAlign = TextAlign.Center,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = item.subtitle,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun StreakItem(day: StreakDay) {
    val imageRes = if (day.isActive) R.drawable.fire_streak else R.drawable.fire_grey
    val imageWidth = if (day.isActive) 44.dp else 34.dp
    val imageHeight = if (day.isActive) 44.dp else 34.dp

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(46.dp)
            .height(76.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(46.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Streak status untuk ${day.dayName}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(imageWidth)
                    .height(imageHeight)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = day.dayName,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                fontWeight = FontWeight(700),
                color = if (day.isActive) Primary500 else Color(0xFF9E9E9E),
                textAlign = TextAlign.Center,
            )
        )
    }
}

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
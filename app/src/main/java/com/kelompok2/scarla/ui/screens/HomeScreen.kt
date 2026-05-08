package com.kelompok2.scarla.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer // Gunakan import ini saja untuk graphicsLayer
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
import android.R.attr.translationZ
import com.kelompok2.scarla.ui.components.AppButton
import com.kelompok2.scarla.ui.components.ButtonType


data class StreakDay(
    val dayName: String,
    val isActive: Boolean
)

data class BeaItem(
    val title: String,
    val subtitle: String,
    val imageRes: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
    val streakDays = listOf(
        StreakDay("Sen", true),
        StreakDay("Sel", false),
        StreakDay("Rab", false),
        StreakDay("Kam", false),
        StreakDay("Jum", false),
        StreakDay("Sab", false),
        StreakDay("Min", false)
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

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(430.dp)
            .height(720.dp)
    ) {
        // Header Logo
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .border(width = 2.dp, Neutral300)
                .padding(2.dp)
                .width(430.dp)
                .height(104.dp)
                .padding(start = 140.dp, top = 10.dp, end = 140.dp, bottom = 10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.font_logo),
                contentDescription = "Logo",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(200.dp)
                    .height(20.dp)
            )
        }

        // Konten Utama
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .width(400.2.dp)
                .height(560.dp)
        ) {
            // Bagian Streak
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(400.2.dp)
                    .height(151.4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(400.2.dp)
                        .height(36.dp)
                ) {
                    Text(
                        text = "Hello, Ahmad",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontWeight = FontWeight(700),
                            color = Neutral900,
                            textAlign = TextAlign.Center,
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_notifications_none_24),
                        contentDescription = "Notifications",
                        modifier = Modifier
                            .width(32.dp)
                            .height(32.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .shadow(elevation = 4.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
                        .width(400.2.dp)
                        .height(103.4.dp)
                        .background(color = Color(0xFFF5F5F5), shape = RoundedCornerShape(size = 12.dp))
                        .padding(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 12.dp)
                ) {
                    streakDays.forEach { item ->
                        StreakItem(day = item)
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .width(400.25571.dp)
                    .height(212.dp)
            ) {
                Text(
                    text = "Lanjut Belajar",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(700),
                        color = Neutral900,
                    )
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(36.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(400.25571.dp)
                        .height(170.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(120.dp)
                            .height(170.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .shadow(elevation = 4.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
                                .width(120.dp)
                                .height(120.dp)
                                .background(color = Color(0xFFF5F5F5), shape = RoundedCornerShape(size = 12.dp))
                                .padding(start = 14.dp, top = 7.dp, end = 14.dp, bottom = 7.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_html),
                                contentDescription = "image description",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .width(92.dp)
                                    .height(87.64497.dp)
                            )
                            Text(
                                text = "Belajar Dasar HTML",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.bubblegum_sans_regular)),
                                    fontWeight = FontWeight(400),
                                    color = Neutral900,
                                )
                            )
                        }
                        AppButton(
                            text = "Continue",
                            buttonType = ButtonType.PRIMARY,
                            onClick = {
//                                route = "informatika_screen"
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Bagian Informasi Beasiswa (Carousel)
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.width(400.2.dp)
            ) {
                Text(
                    text = "Informasi Beasiswa",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(700),
                        color = Neutral900,
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .height(30.dp)
                )

                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 92.dp),
                    pageSpacing = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
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
                        stop = 8f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                    BeasiswaCard(
                        item = scholarshipList[page],
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                this.alpha = alpha
                                // Properti translationX/Y/Z di dalam scope graphicsLayer dapat langsung diakses & diubah tanpa error
                                val translationZ = (1f - pageOffset.coerceIn(0f, 1f)) * 10f
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

@Composable
fun BeasiswaCard(
    item: BeaItem,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(210.dp)
            .height(330.dp)
            .background(color = Color.White, shape = RoundedCornerShape(size = 24.dp))
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(18.dp))
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = item.title,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF303030),
                    textAlign = TextAlign.Center,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = item.subtitle,
                style = TextStyle(
                    fontSize = 11.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF757575),
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
    val imageWidth = if (day.isActive) 47.4.dp else 34.4.dp
    val imageHeight = if (day.isActive) 47.4.dp else 34.4.dp

    Column(
        verticalArrangement = Arrangement.spacedBy(13.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(47.4.dp)
            .height(79.4.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Streak status untuk ${day.dayName}",
            contentScale = ContentScale.None,
            modifier = Modifier
                .padding(1.dp)
                .width(imageWidth)
                .height(imageHeight)
        )
        Text(
            text = day.dayName,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.bubblegum_sans_regular)),
                fontWeight = FontWeight(400),
                color = Primary500,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .width(23.dp)
                .height(19.dp)
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
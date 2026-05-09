package com.kelompok2.scarla.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.kelompok2.scarla.R
import com.kelompok2.scarla.ui.theme.*
import kotlin.math.absoluteValue
import com.kelompok2.scarla.ui.components.AppButton
import com.kelompok2.scarla.ui.components.ButtonType


// --- DATA MODELS ---

data class AchievementItem(
    val title: String,
    val subtitle: String,
    val imageRes: Int,
)


object Variables {
    val Margin: Dp = 16.dp
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AchievementPage(
    navController: NavController? = null,
    onBack: (() -> Unit)? = null,
) {
    val scrollState = rememberScrollState()

    val achievementItem = listOf(
        AchievementItem("Perjalanan Pemula", "Selesaikan pelajaran pertama", R.drawable.achievement_1),
        AchievementItem("Bola Api", "Streak selama 3 hari", R.drawable.achievement_2),
        AchievementItem("Kembang Api", "Streak selama 14 hari", R.drawable.achievement_3),
        AchievementItem("Komet", "Streak selama 30 hari", R.drawable.achievement_4),
        AchievementItem("Meteor", "Streak selama 2 bulan", R.drawable.achievement_5),
        AchievementItem("Si Paling Ambis", "Menyelesaikan 10 pelajaran", R.drawable.achievement_6),
        AchievementItem("Si Paling Friendly", "Memiliki 10 Teman", R.drawable.achievement_7),
    )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // Header dengan tombol back
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = Variables.Margin, end = Variables.Margin, top = 16.dp)
        ) {
            IconButton(
                onClick = {
                    if (navController != null) {
                        navController.popBackStack()
                    } else if (onBack != null) {
                        onBack()
                    }
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Color(0xFFFFC300), shape = RoundedCornerShape(size = 44.dp))
                    .border(width = 1.dp, color = Color(0xFF303030), shape = RoundedCornerShape(size = 44.dp))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Neutral800,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = "Achievement",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(700),
                    color = Neutral900,
                ),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Achievements Section
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Variables.Margin)
        ) {
            Text(
                text = "Pencapaian",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(600),
                    color = Neutral900,
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                achievementItem.forEach { item ->
                    AchievementCard(item = item)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AchievementCard(
    item: AchievementItem,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .shadow(elevation = 8.dp, spotColor = Color(0x26000000), ambientColor = Color(0x26000000), shape = RoundedCornerShape(16.dp))
            .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
            .padding(start = 12.dp, end = 12.dp)
    ) {
        // Gambar
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        // Teks dan Progress
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(600),
                    color = Neutral900,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.subtitle,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF707070),
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            // Progress Bar dan Angka
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .weight(1f)
                        .background(color = Color(0xFFE0E0E0), shape = RoundedCornerShape(2.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(color = Color(0xFFFFC300), shape = RoundedCornerShape(2.dp))
                    )
                }

                Text(
                    text = "0/1",
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF808080),
                    ),
                )
            }
        }
    }
}
package com.kelompok2.scarla.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kelompok2.scarla.R
import com.kelompok2.scarla.navigation.Screen
import com.kelompok2.scarla.ui.theme.Neutral900

// ──────────────────────────────────────────────────────────────────────────────
// DATA MODELS — Komunitas
// ──────────────────────────────────────────────────────────────────────────────

data class KomunitasSubChannel(
    val name: String,
    val lastMsg: String,
    val time: String,
    val hasUnread: Boolean
)

data class KomunitasDiikutiData(
    val name: String,
    val iconRes: Int,
    val communityId: String,
    val channels: List<KomunitasSubChannel>
)

data class KomunitasDiscoverData(
    val name: String,
    val iconRes: Int
)

// ──────────────────────────────────────────────────────────────────────────────
// DATA STATIS — akan digantikan Firebase di iterasi berikutnya
// ──────────────────────────────────────────────────────────────────────────────

val defaultJoinedKomunitas = listOf(
    KomunitasDiikutiData(
        name = "Si Paling Ambis",
        iconRes = R.drawable.ic_informatika,
        communityId = "si_paling_ambis",
        channels = listOf(
            KomunitasSubChannel("Introduction", "Aziz Shloln bergabung", "09:00", false),
            KomunitasSubChannel("Ruang Diskusi", "Indika Putra Praisah: Guys ngoding yuk!", "10:00", true)
        )
    )
)

val defaultDiscoverKomunitas = listOf(
    KomunitasDiscoverData("Pecinta Matematika", R.drawable.ic_matematika),
    KomunitasDiscoverData("Pecinta Fisika", R.drawable.ic_fisika)
)

// ──────────────────────────────────────────────────────────────────────────────
// KOMUNITAS TAB CONTENT — Entry point composable
// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun KomunitasTabContent(navController: NavController? = null) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Section: Komunitas yang kamu ikuti ─────────────────────────
        Text(
            text = "Komunitas yang kamu ikuti",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                fontWeight = FontWeight(700),
                color = Neutral900
            )
        )

        defaultJoinedKomunitas.forEach { komunitas ->
            KomunitasDiikutiCard(komunitas, navController)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ── Section: Temukan komunitas ─────────────────────────────────
        Text(
            text = "Temukan komunitas",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                fontWeight = FontWeight(700),
                color = Neutral900
            )
        )

        defaultDiscoverKomunitas.forEach { komunitas ->
            KomunitasDiscoverCard(komunitas)
        }

        // ── Tombol Jelajahi Lainnya ────────────────────────────────────
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFFFFDD00))
                    .clickable { /* TODO: navigate to explore all */ }
                    .padding(horizontal = 28.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Jelajahi Lainnya",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(700),
                        color = Neutral900
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// KOMUNITAS DIIKUTI CARD
// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun KomunitasDiikutiCard(
    komunitas: KomunitasDiikutiData,
    navController: NavController? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFFFCC00))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
            // ── Header komunitas ──────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = komunitas.iconRes),
                        contentDescription = komunitas.name,
                        modifier = Modifier.size(36.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Text(
                    text = komunitas.name,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(700),
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Divider putih
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.6f))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Channel list ──────────────────────────────────────────────
            komunitas.channels.forEach { channel ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            // Navigasi ke KomunitasRoomPage
                            navController?.navigate(
                                Screen.KomunitasRoom.createRoute(
                                    communityName = komunitas.name,
                                    channelName = channel.name
                                )
                            )
                        }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Icon channel
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (channel.name == "Introduction")
                                Icons.Default.Campaign
                            else Icons.Default.Forum,
                            contentDescription = channel.name,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Nama & preview pesan
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = channel.name,
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                fontWeight = FontWeight(700),
                                color = Color.White
                            )
                        )
                        Text(
                            text = channel.lastMsg,
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = Color.White.copy(alpha = 0.85f)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Waktu & unread indicator
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = channel.time,
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = Color.White
                            )
                        )
                        if (channel.hasUnread) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF6B00))
                                    .align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// KOMUNITAS DISCOVER CARD
// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun KomunitasDiscoverCard(komunitas: KomunitasDiscoverData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFFFCC00))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon komunitas
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = komunitas.iconRes),
                    contentDescription = komunitas.name,
                    modifier = Modifier.size(36.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Nama komunitas
            Text(
                text = komunitas.name,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(700),
                    color = Color.White
                )
            )

            // Tombol Ikuti (UI only)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFDD00))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE6B800),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { /* TODO: join komunitas */ }
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ikuti",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(700),
                        color = Neutral900
                    )
                )
            }
        }
    }
}

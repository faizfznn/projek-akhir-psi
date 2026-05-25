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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kelompok2.scarla.R
import com.kelompok2.scarla.navigation.Screen
import com.kelompok2.scarla.ui.theme.Neutral900
import com.kelompok2.scarla.ui.viewmodel.CommunityChannel
import com.kelompok2.scarla.ui.viewmodel.CommunityData
import com.kelompok2.scarla.ui.viewmodel.KomunitasViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ──────────────────────────────────────────────────────────────────────────────
// KOMUNITAS TAB CONTENT — Entry point composable
// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun KomunitasTabContent(
    navController: NavController? = null,
    komunitasViewModel: KomunitasViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val uiState by komunitasViewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFFFCC00))
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Section: Komunitas yang kamu ikuti ─────────────────────────
        if (uiState.joinedCommunities.isNotEmpty()) {
            Text(
                text = "Komunitas yang kamu ikuti",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(700),
                    color = Neutral900
                )
            )

            uiState.joinedCommunities.forEach { komunitas ->
                KomunitasDiikutiCard(
                    komunitas = komunitas,
                    channels = uiState.channels,
                    activeCommunityId = uiState.activeCommunityId,
                    navController = navController,
                    onCardClick = {
                        komunitasViewModel.openCommunityChannels(komunitas.id, komunitas.name)
                    }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
        }

        // ── Section: Temukan komunitas ─────────────────────────────────
        if (uiState.discoverCommunities.isNotEmpty()) {
            Text(
                text = "Temukan komunitas",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(700),
                    color = Neutral900
                )
            )

            // Show max 2 discover communities in main tab
            uiState.discoverCommunities.take(2).forEach { komunitas ->
                KomunitasDiscoverCard(
                    komunitas = komunitas,
                    isJoining = komunitas.id in uiState.isJoining,
                    onJoin = { komunitasViewModel.joinCommunity(komunitas.id) }
                )
            }
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
                    .clickable {
                        navController?.navigate(Screen.JelajahiKomunitas.route)
                    }
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
    komunitas: CommunityData,
    channels: List<CommunityChannel>,
    activeCommunityId: String?,
    navController: NavController? = null,
    onCardClick: () -> Unit
) {
    val context = LocalContext.current
    val iconResId = remember(komunitas.iconRes) {
        if (komunitas.iconRes.isNotBlank())
            context.resources.getIdentifier(komunitas.iconRes, "drawable", context.packageName)
        else 0
    }

    // Load channels when this card is first shown
    LaunchedEffect(komunitas.id) {
        if (activeCommunityId != komunitas.id) {
            onCardClick()
        }
    }

    // Only show channels for this community
    val communityChannels = if (activeCommunityId == komunitas.id) channels else emptyList()

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
                    if (iconResId != 0) {
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = komunitas.name,
                            modifier = Modifier.size(36.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Text(
                            text = komunitas.name.firstOrNull()?.uppercase() ?: "?",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                fontWeight = FontWeight(700),
                                color = Neutral900
                            )
                        )
                    }
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
            communityChannels.forEach { channel ->
                val timeStr = remember(channel.lastMessageAt) {
                    if (channel.lastMessageAt > 0L)
                        SimpleDateFormat("HH:mm", Locale("id")).format(Date(channel.lastMessageAt))
                    else ""
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            navController?.navigate(
                                Screen.KomunitasRoom.createRoute(
                                    communityId = komunitas.id,
                                    channelId = channel.id,
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
                            imageVector = if (channel.type == "announcement")
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
                        if (channel.lastMessage.isNotBlank()) {
                            Text(
                                text = channel.lastMessage,
                                style = TextStyle(
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    color = Color.White.copy(alpha = 0.85f)
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Waktu
                    if (timeStr.isNotBlank()) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = timeStr,
                                style = TextStyle(
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    color = Color.White
                                )
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
fun KomunitasDiscoverCard(
    komunitas: CommunityData,
    isJoining: Boolean = false,
    onJoin: () -> Unit = {}
) {
    val context = LocalContext.current
    val iconResId = remember(komunitas.iconRes) {
        if (komunitas.iconRes.isNotBlank())
            context.resources.getIdentifier(komunitas.iconRes, "drawable", context.packageName)
        else 0
    }

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
                if (iconResId != 0) {
                    Image(
                        painter = painterResource(id = iconResId),
                        contentDescription = komunitas.name,
                        modifier = Modifier.size(36.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text(
                        text = komunitas.name.firstOrNull()?.uppercase() ?: "?",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontWeight = FontWeight(700),
                            color = Neutral900
                        )
                    )
                }
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

            // Tombol Ikuti
            if (isJoining) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFDD00))
                        .padding(horizontal = 18.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Neutral900,
                        strokeWidth = 2.dp
                    )
                }
            } else if (komunitas.isJoined) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Bergabung ✓",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontWeight = FontWeight(700),
                            color = Color.White
                        )
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFDD00))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE6B800),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onJoin() }
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
}

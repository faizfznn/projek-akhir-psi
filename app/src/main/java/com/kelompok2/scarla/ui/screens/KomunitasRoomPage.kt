package com.kelompok2.scarla.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.kelompok2.scarla.R
import com.kelompok2.scarla.ui.theme.Neutral500
import com.kelompok2.scarla.ui.theme.Neutral600
import com.kelompok2.scarla.ui.theme.Neutral900
import com.kelompok2.scarla.ui.viewmodel.CommunityMessage
import com.kelompok2.scarla.ui.viewmodel.KomunitasViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ──────────────────────────────────────────────────────────────────────────────
// MAIN SCREEN
// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun KomunitasRoomPage(
    communityId: String,
    channelId: String,
    communityName: String,
    channelName: String,
    navController: NavController? = null,
    komunitasViewModel: KomunitasViewModel = viewModel()
) {
    val listState = rememberLazyListState()
    val uiState by komunitasViewModel.uiState.collectAsStateWithLifecycle()
    val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Start listening to messages when entering the room
    LaunchedEffect(communityId, channelId) {
        komunitasViewModel.openChannel(communityId, channelId, communityName, channelName)
    }

    // Clean up listener when leaving the room
    DisposableEffect(Unit) {
        onDispose {
            komunitasViewModel.closeChannel()
        }
    }

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDBD42E))
            .navigationBarsPadding()
            .imePadding()
    ) {
        // ── LOGO SCARLA (White background) ─────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 12.dp, end = 12.dp, top = 30.dp, bottom = 1.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.scarla_logo),
                contentDescription = "Scarla Logo",
                modifier = Modifier.size(width = 160.dp, height = 70.dp),
                contentScale = ContentScale.Fit
            )
        }

        // ── HEADER ─────────────────────────────────────────────────────
        KomunitasRoomHeader(
            communityName = communityName,
            channelName = channelName,
            onBack = { navController?.popBackStack() }
        )

        // ── MESSAGE LIST ───────────────────────────────────────────────
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.messages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada pesan. Mulai percakapan! 💬",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            } else {
                items(uiState.messages, key = { it.id }) { msg ->
                    CommunityMessageBubble(
                        message = msg,
                        isMe = msg.senderUid == currentUid
                    )
                }
            }
        }

        // ── INPUT BAR ──────────────────────────────────────────────────
        KomunitasInputBar(
            value = uiState.currentMessage,
            onValueChange = { komunitasViewModel.onMessageChange(it) },
            onSend = { komunitasViewModel.sendMessage() }
        )
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// HEADER
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun KomunitasRoomHeader(
    communityName: String,
    channelName: String,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(Color(0xFFDBD42E))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // ── Back Button ───────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFC300))
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color(0xFF1A1A1A),
                    modifier = Modifier.size(22.dp)
                )
            }

            // ── Channel Icon + Name (Tengah) ──────────────────────────
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Megaphone icon dalam lingkaran kuning gelap
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFC300)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Channel Icon",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(
                        text = channelName,
                        style = TextStyle(
                            fontSize = 17.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFF1A1A1A)
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = communityName,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            color = Color(0xFF3A3A3A)
                        ),
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(44.dp)) // balance back button width
        }

        // ── Info Button (pojok kanan atas — rounded corner) ───────────
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(bottomStart = 35.dp))
                .background(Color(0xFFFFDD00))
                .padding(start = 3.dp, bottom = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomStart = 32.dp))
                    .background(Color.White)
                    .padding(start = 22.dp, bottom = 22.dp, end = 16.dp, top = 14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFC300))
                        .clickable { /* TODO: show group info */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info Grup",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// COMMUNITY MESSAGE BUBBLE
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun CommunityMessageBubble(message: CommunityMessage, isMe: Boolean) {
    val shape = RoundedCornerShape(24.dp)
    val bubbleColor = if (isMe) Color(0xFFC0F5AA) else Color.White

    val timeStr = if (message.timestampMillis > 0L)
        SimpleDateFormat("HH:mm", Locale("id")).format(Date(message.timestampMillis))
    else ""

    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut()
    ) {
        if (isMe) {
            // Pesan kita — rata kanan, tanpa nama & avatar
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(max = 280.dp)
                        .shadow(elevation = 3.dp, shape = shape)
                        .background(bubbleColor, shape)
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = message.text,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = Neutral900
                            )
                        )
                        Text(
                            text = timeStr,
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = Neutral600
                            ),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        } else {
            // Pesan orang lain — rata kiri, dengan avatar & nama
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Avatar (initial-based)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF0A0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = message.senderName.firstOrNull()?.uppercase() ?: "?",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontWeight = FontWeight(700),
                            color = Neutral900
                        )
                    )
                }

                // Bubble
                Column(
                    modifier = Modifier.widthIn(max = 260.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Nama pengirim
                    Text(
                        text = message.senderName,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontWeight = FontWeight(700),
                            color = Color.White
                        )
                    )

                    Box(
                        modifier = Modifier
                            .shadow(elevation = 3.dp, shape = shape)
                            .background(bubbleColor, shape)
                            .padding(horizontal = 18.dp, vertical = 12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = message.text,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    color = Neutral900
                                )
                            )
                            Text(
                                text = timeStr,
                                style = TextStyle(
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    color = Neutral600
                                ),
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// INPUT BAR
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun KomunitasInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFDBD42E))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            placeholder = {
                Text(
                    "Ketik pesan",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = Neutral500
                    )
                )
            },
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                color = Neutral900
            ),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { if (value.isNotBlank()) onSend() })
        )

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(enabled = value.isNotBlank()) { onSend() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Kirim",
                tint = Color(0xFFFFDD00),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavController
import com.kelompok2.scarla.R
import com.kelompok2.scarla.ui.theme.Neutral500
import com.kelompok2.scarla.ui.theme.Neutral600
import com.kelompok2.scarla.ui.theme.Neutral900

// ──────────────────────────────────────────────────────────────────────────────
// DATA MODEL — Group Message (statis / UI only)
// ──────────────────────────────────────────────────────────────────────────────

data class GroupMessage(
    val id: String,
    val senderName: String,
    val senderAvatar: Int,   // drawable res id, 0 = pakai inisial
    val text: String,
    val time: String,
    val isMe: Boolean = false
)

// ──────────────────────────────────────────────────────────────────────────────
// DUMMY DATA per channel
// ──────────────────────────────────────────────────────────────────────────────

private fun getDummyMessages(channelName: String): List<GroupMessage> {
    return if (channelName == "Introduction") {
        listOf(
            GroupMessage(
                id = "1",
                senderName = "Alief Hikmawan",
                senderAvatar = R.drawable.avatar_1,
                text = """📋 Peraturan Grup Si Paling Ambis :
1. Fokus Produktif – Bahas target, progres, dan ilmu. No mager talk.
2. Aksi > Wacana – Share progress, bukan cuma rencana.
3. Saling Dukung – Kritik membangun, bukan nyinyir.
4. Ikut Tantangan – Ada challenge mingguan, wajib coba!
5. Share Ilmu – Temu info bermanfaat? Bagikan ke grup.
6. Minim Spam – Meme & chat random? Ada waktunya.
7. Tepat Waktu – Deadlines dan diskusi jangan ngaret.
8. Evaluasi Rutin – Cek progres tiap minggu.
9. Jangan Hilang – Lagi drop? Ngaku, biar disemangatin.""",
                time = "09:00",
                isMe = false
            )
        )
    } else {
        listOf(
            GroupMessage(
                id = "1",
                senderName = "Indika Putra",
                senderAvatar = R.drawable.avatar_2,
                text = "Guys ngoding yuk! Ada yang mau pair programming bareng?",
                time = "10:00",
                isMe = false
            ),
            GroupMessage(
                id = "2",
                senderName = "Kamu",
                senderAvatar = 0,
                text = "Siap! Lagi ngerjain apa sekarang?",
                time = "10:02",
                isMe = true
            ),
            GroupMessage(
                id = "3",
                senderName = "Indika Putra",
                senderAvatar = R.drawable.avatar_2,
                text = "Lagi ngerjain fitur auth di Android. Boleh bantu review?",
                time = "10:03",
                isMe = false
            )
        )
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// MAIN SCREEN
// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun KomunitasRoomPage(
    communityName: String,
    channelName: String,
    navController: NavController? = null
) {
    val listState = rememberLazyListState()
    val messages = remember(channelName) { getDummyMessages(channelName) }
    var inputText by remember { mutableStateOf("") }

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
            items(messages, key = { it.id }) { msg ->
                GroupMessageBubble(message = msg)
            }
        }

        // ── INPUT BAR ──────────────────────────────────────────────────
        KomunitasInputBar(
            value = inputText,
            onValueChange = { inputText = it },
            onSend = {
                // UI only — tidak kirim ke Firebase
                inputText = ""
            }
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
// GROUP MESSAGE BUBBLE
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun GroupMessageBubble(message: GroupMessage) {
    val shape = RoundedCornerShape(24.dp)
    val bubbleColor = if (message.isMe) Color(0xFFC0F5AA) else Color.White

    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut()
    ) {
        if (message.isMe) {
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
                            text = message.time,
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
                // Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF0A0)),
                    contentAlignment = Alignment.Center
                ) {
                    if (message.senderAvatar != 0) {
                        Image(
                            painter = painterResource(id = message.senderAvatar),
                            contentDescription = message.senderName,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
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
                                text = message.time,
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

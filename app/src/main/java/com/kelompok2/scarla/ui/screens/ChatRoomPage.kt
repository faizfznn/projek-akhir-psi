package com.kelompok2.scarla.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.kelompok2.scarla.R
import com.kelompok2.scarla.navigation.Screen
import com.kelompok2.scarla.ui.theme.Neutral500
import com.kelompok2.scarla.ui.theme.Neutral600
import com.kelompok2.scarla.ui.theme.Neutral900
import com.kelompok2.scarla.ui.theme.Secondary500
import com.kelompok2.scarla.ui.viewmodel.ChatMessage
import com.kelompok2.scarla.ui.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.kelompok2.scarla.ui.components.ReportAccountDialog
import com.kelompok2.scarla.ui.components.ReportThanksDialog

@Composable
fun ChatRoomPage(
        peerUid: String,
        peerName: String,
        peerAvatar: String = "",
        navController: NavController? = null,
        viewModel: ChatViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val myUid = remember { FirebaseAuth.getInstance().currentUser?.uid ?: "" }
    val listState = rememberLazyListState()
    var showReportDialog by remember { mutableStateOf(false) }
    var showThanksDialog by remember { mutableStateOf(false) }

    LaunchedEffect(peerUid) {
        viewModel.openChat(peerUid = peerUid, peerName = peerName, peerAvatar = peerAvatar)
    }

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(
            modifier =
                    Modifier.fillMaxSize()
                            .background(Color(0xFFDBD42E))
                            .navigationBarsPadding()
                            .imePadding()
    ) {
        // ── LOGO SCARLA (White background) ─────────────────────────────
        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .background(Color.White)
                                .padding(start = 12.dp, end = 12.dp, top = 30.dp, bottom = 1.dp)
        ) {
            androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.scarla_logo),
                    contentDescription = "Scarla Logo",
                    modifier = Modifier.size(width = 160.dp, height = 70.dp),
                    contentScale = ContentScale.Fit
            )
        }

        // ── HEADER dengan gradient kuning ──────────────────────────────
        ChatRoomHeaderStyled(
                peerName = uiState.activePeerName.ifBlank { peerName },
                peerAvatar = uiState.activePeerAvatar.ifBlank { peerAvatar },
                isOnline = uiState.activePeerIsOnline,
                onBack = {
                    viewModel.closeChat()
                    navController?.popBackStack()
                },
                onProfileClick = {
                    navController?.navigate(Screen.PeerProfile.createRoute(peerUid))
                },
                onReportClick = { showReportDialog = true }
        )

        // ── MESSAGE LIST ───────────────────────────────────────────────
        Box(modifier = Modifier.weight(1f)) {
            if (uiState.isLoadingMessages) {
                CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Secondary500
                )
            } else if (uiState.messages.isEmpty()) {
                EmptyChatRoomState(peerName)
            } else {
                LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.messages, key = { it.id }) { msg ->
                        MessageBubbleStyled(message = msg, isMe = msg.senderUid == myUid)
                    }
                }
            }
        }

        // ── INPUT BAR ──────────────────────────────────────────────────
        ChatInputBarStyled(
                value = uiState.currentMessage,
                onValueChange = viewModel::onMessageChange,
                onSend = viewModel::sendMessage
        )

        if (showReportDialog) {
            ReportAccountDialog(
                    targetName =
                            (uiState.activePeerName.ifBlank { peerName }).ifBlank { "akun ini" },
                    onDismiss = { showReportDialog = false },
                    onReportSubmitted = { _ ->
                        showReportDialog = false
                        showThanksDialog = true
                    }
            )
        }

        if (showThanksDialog) {
            ReportThanksDialog(onDismiss = { showThanksDialog = false })
        }
    }
}

@Composable
private fun ChatRoomHeaderStyled(
        peerName: String,
        peerAvatar: String,
        isOnline: Boolean,
        onBack: () -> Unit,
        onProfileClick: () -> Unit,
        onReportClick: () -> Unit
) {
    val context = LocalContext.current
    val avatarRes =
            remember(peerAvatar) {
                if (peerAvatar.isNotBlank())
                        context.resources.getIdentifier(peerAvatar, "drawable", context.packageName)
                else 0
            }

    Box(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                            .background(Color(0xFFDBD42E))
    ) {
        // Baris Utama: Back Button dan Profil
        Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back Button
            Box(
                    modifier =
                            Modifier.size(44.dp)
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

            // Info Profil (Tengah)
            Row(
                    modifier =
                            Modifier.weight(1f)
                                    .clickable { onProfileClick() }
                                    .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                        modifier =
                                Modifier.size(64.dp)
                                        .clip(CircleShape)
                                        .border(3.dp, Color.White, CircleShape)
                                        .background(Color.White),
                        contentAlignment = Alignment.Center
                ) {
                    if (avatarRes != 0) {
                        androidx.compose.foundation.Image(
                                painter = painterResource(avatarRes),
                                contentDescription = peerName,
                                modifier = Modifier.size(64.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                                text = peerName.firstOrNull()?.uppercase() ?: "?",
                                style =
                                        TextStyle(
                                                fontSize = 24.sp,
                                                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                                fontWeight = FontWeight(700),
                                                color = Color(0xFF1A1A1A)
                                        )
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                            text = peerName.ifBlank { "Pengguna" },
                            style =
                                    TextStyle(
                                            fontSize = 17.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                            fontWeight = FontWeight(700),
                                            color = Color(0xFF1A1A1A)
                                    ),
                            maxLines = 1
                    )
                    Text(
                            text = if (isOnline) "Online" else "Offline",
                            style =
                                    TextStyle(
                                            fontSize = 13.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                            color = Color(0xFF3A3A3A)
                                    )
                    )
                }
            }
            Spacer(modifier = Modifier.width(70.dp))
        }

        Box(
                modifier =
                        Modifier.align(Alignment.TopEnd)
                                .clip(RoundedCornerShape(bottomStart = 35.dp))
                                .background(Color(0xFFFFDD00))
                                .padding(start = 3.dp, bottom = 3.dp)
        ) {
            Box(
                    modifier =
                            Modifier.clip(RoundedCornerShape(bottomStart = 32.dp))
                                    .background(Color.White)
                                    .padding(
                                            start = 22.dp,
                                            bottom = 22.dp,
                                            end = 16.dp,
                                            top = 14.dp
                                    )
            ) {
                Box(
                        modifier =
                                Modifier.size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFC300))
                                        .clickable { onReportClick() },
                        contentAlignment = Alignment.Center
                ) {
                    Text(
                            text = "!",
                            style =
                                    TextStyle(
                                            fontSize = 24.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                            fontWeight = FontWeight(900),
                                            color = Color(0xFF1A1A1A)
                                    )
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubbleStyled(message: ChatMessage, isMe: Boolean) {
    val bubbleColor = if (isMe) Color(0xFFC0F5AA) else Color.White
    val textColor = Neutral900
    val alignment = if (isMe) Alignment.End else Alignment.Start
    val shape = RoundedCornerShape(24.dp)

    val timeStr =
            remember(message.timestampMillis) {
                if (message.timestampMillis > 0L)
                        SimpleDateFormat("HH:mm", Locale("id"))
                                .format(Date(message.timestampMillis))
                else ""
            }

    AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut()
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
            Box(
                    modifier =
                            Modifier.widthIn(max = 280.dp)
                                    .shadow(elevation = 3.dp, shape = shape)
                                    .background(bubbleColor, shape)
                                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                            text = message.text,
                            style =
                                    TextStyle(
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                            color = textColor
                                    )
                    )
                    if (timeStr.isNotBlank()) {
                        Text(
                                text = timeStr,
                                style =
                                        TextStyle(
                                                fontSize = 11.sp,
                                                fontFamily =
                                                        FontFamily(Font(R.font.poppins_regular)),
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

@Composable
private fun ChatInputBarStyled(value: String, onValueChange: (String) -> Unit, onSend: () -> Unit) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .background(Color(0xFFDBD42E))
                            .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f).height(56.dp),
                placeholder = {
                    Text(
                            "Ketik pesan",
                            style =
                                    TextStyle(
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                            color = Neutral500
                                    )
                    )
                },
                shape = RoundedCornerShape(28.dp),
                colors =
                        OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                        ),
                textStyle =
                        TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = Neutral900
                        ),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { if (value.isNotBlank()) onSend() })
        )

        // Send Button
        Box(
                modifier =
                        Modifier.size(48.dp).clip(CircleShape).background(Color.White).clickable(
                                        enabled = value.isNotBlank()
                                ) { onSend() },
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

@Composable
private fun EmptyChatRoomState(peerName: String) {
    Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Box(
                modifier =
                        Modifier.clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFFFFFCC).copy(alpha = 0.5f))
                                .padding(24.dp)
        ) {
            Text(
                    text = "Permintaanmu telah disetujui\nAyo mulai percakapan!",
                    style =
                            TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                    fontWeight = FontWeight(600),
                                    color = Neutral900,
                                    textAlign = TextAlign.Center
                            )
            )
        }
    }
}

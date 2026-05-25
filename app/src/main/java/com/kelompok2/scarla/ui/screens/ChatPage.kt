package com.kelompok2.scarla.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kelompok2.scarla.R
import com.kelompok2.scarla.navigation.Screen
import com.kelompok2.scarla.ui.theme.Neutral300
import com.kelompok2.scarla.ui.theme.Neutral500
import com.kelompok2.scarla.ui.theme.Neutral600
import com.kelompok2.scarla.ui.theme.Neutral700
import com.kelompok2.scarla.ui.theme.Neutral900
import com.kelompok2.scarla.ui.theme.Primary500
import com.kelompok2.scarla.ui.viewmodel.ChatContact
import com.kelompok2.scarla.ui.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ──────────────────────────────────────────────────────────────────────────────
// ENTRY POINT — Rute: "chat_list"
// Menampilkan daftar percakapan yang dimiliki user saat ini.
// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun ChatPage(
        navController: NavController? = null,
        viewModel: ChatViewModel = viewModel(),
        onUnreadCountChange: (Int) -> Unit = {}
) {
    // REAKTIF: collectAsStateWithLifecycle → UI otomatis update saat ada chat baru
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contacts by viewModel.contactsFlow.collectAsStateWithLifecycle()

    LaunchedEffect(contacts) {
        val totalUnread = contacts.sumOf { it.unreadCount }
        onUnreadCountChange(totalUnread)
    }

    // STATE untuk Tab & Search
    var selectedTab by remember { mutableStateOf(0) } // 0 = Chat, 1 = Komunitas
    var searchQuery by remember { mutableStateOf("") }

    // Filter contacts berdasarkan search (hanya ketika tab Chat aktif)
    val filteredContacts =
            if (selectedTab == 0 && searchQuery.isEmpty()) {
                contacts
            } else if (selectedTab == 0) {
                contacts.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                            it.lastMessage.contains(searchQuery, ignoreCase = true)
                }
            } else {
                emptyList() // Tab Komunitas tidak butuh search
            }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // ── Header dengan Title ─────────────────────────────────────────
        Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                    text = "Pesan",
                    style =
                            TextStyle(
                                    fontSize = 28.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                    fontWeight = FontWeight(700),
                                    color = Neutral900
                            )
            )

            // Icons (notification & settings) - bisa implementasikan nanti
            Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(end = 4.dp)
            ) {
                IconButton(onClick = {}) {
                    Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notifikasi",
                            tint = Neutral900,
                            modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = { navController?.navigate(Screen.Settings.route) }) {
                    Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Pengaturan",
                            tint = Neutral900,
                            modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // ── Tab Navigation ─────────────────────────────────────────────
        Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(52.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tab Chat
            Box(
                    modifier =
                            Modifier.weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(28.dp))
                                    .background(
                                            if (selectedTab == 0) Color(0xFFFFDD00)
                                            else Color.Transparent
                                    )
                                    .clickable {
                                        selectedTab = 0
                                        searchQuery = "" // Reset search saat ganti tab
                                    }
                                    .border(
                                            width = 3.dp,
                                            color = Color(0xFFFFEA00),
                                            shape = RoundedCornerShape(28.dp)
                                    ),
                    contentAlignment = Alignment.Center
            ) {
                Text(
                        text = "Chat",
                        style =
                                TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                        fontWeight = FontWeight(700),
                                        color = Neutral900
                                )
                )
            }

            // Tab Komunitas
            Box(
                    modifier =
                            Modifier.weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(28.dp))
                                    .background(
                                            if (selectedTab == 1) Color(0xFFFFDD00)
                                            else Color.Transparent
                                    )
                                    .clickable {
                                        selectedTab = 1
                                        searchQuery = "" // Reset search saat ganti tab
                                    }
                                    .border(
                                            width = 3.dp,
                                            color = Color(0xFFFFEA00),
                                            shape = RoundedCornerShape(28.dp)
                                    ),
                    contentAlignment = Alignment.Center
            ) {
                Text(
                        text = "Komunitas",
                        style =
                                TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                        fontWeight = FontWeight(700),
                                        color = Neutral900
                                )
                )
            }
        }

        // ── Search Bar (Hanya muncul di Tab Chat) ───────────────────────
        if (selectedTab == 0) {
            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier =
                            Modifier.fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                    placeholder = {
                        Text(
                                text = "Cari chat...",
                                style = TextStyle(fontSize = 14.sp, color = Neutral500)
                        )
                    },
                    leadingIcon = {
                        Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Neutral500,
                                modifier = Modifier.size(20.dp)
                        )
                    },
                    colors =
                            TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color(0xFFF5F5F5),
                                    focusedContainerColor = Color(0xFFF5F5F5),
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    cursorColor = Neutral900
                            ),
                    textStyle = TextStyle(fontSize = 14.sp, color = Neutral900),
                    singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ── Konten (Tab-based) ──────────────────────────────────────────
        if (selectedTab == 0) {
            // TAB CHAT
            if (filteredContacts.isEmpty() && searchQuery.isNotEmpty()) {
                // Hasil search kosong
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                            text = "Tidak ada chat yang ditemukan",
                            style = TextStyle(fontSize = 14.sp, color = Neutral500)
                    )
                }
            } else if (contacts.isEmpty()) {
                // Benar-benar tidak ada chat
                EmptyChatState()
            } else {
                LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp), // Jarak antar card chat
                        contentPadding =
                                PaddingValues(
                                        horizontal = 20.dp,
                                        vertical = 16.dp
                                ) // Padding ke layar
                ) {
                    items(filteredContacts, key = { it.uid }) { contact ->
                        Box(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .border(
                                                        width = 3.dp,
                                                        color = Color(0xFFFFDD00),
                                                        shape = RoundedCornerShape(28.dp)
                                                )
                                                .clip(RoundedCornerShape(28.dp))
                                                .background(
                                                        brush =
                                                                Brush.horizontalGradient(
                                                                        colors =
                                                                                listOf(
                                                                                        Color.White, // Warna di sisi kiri
                                                                                        Color(
                                                                                                0xFFFFF7B0
                                                                                        ) // Warna
                                                                                        // di sisi
                                                                                        // kanan
                                                                                        // (kuning
                                                                                        // soft).
                                                                                        // Silakan
                                                                                        // sesuaikan
                                                                                        // kode HEX
                                                                                        // ini
                                                                                        // dengan
                                                                                        // figma
                                                                                        )
                                                                )
                                                )
                        ) {
                            ChatContactCardStyled(
                                    contact = contact,
                                    onClick = {
                                        viewModel.openChat(
                                                peerUid = contact.uid,
                                                peerName = contact.name,
                                                peerAvatar = contact.avatarUrl
                                        )
                                        navController?.navigate(
                                                Screen.ChatRoom.createRoute(
                                                        contact.uid,
                                                        contact.name
                                                )
                                        )
                                    }
                            )
                        }
                    }
                }
            }
        } else {
            // TAB KOMUNITAS — UI di KomunitasPage.kt
            KomunitasTabContent(navController = navController)
        }
    }
}

@Composable
private fun ChatContactCardStyled(contact: ChatContact, onClick: () -> Unit) {
    val context = LocalContext.current
    val avatarRes =
            remember(contact.avatarUrl) {
                if (contact.avatarUrl.isNotBlank())
                        context.resources.getIdentifier(
                                contact.avatarUrl,
                                "drawable",
                                context.packageName
                        )
                else 0
            }

    val timeStr =
            remember(contact.lastMessageMillis) {
                if (contact.lastMessageMillis > 0L)
                        SimpleDateFormat("HH:mm", Locale("id"))
                                .format(Date(contact.lastMessageMillis))
                else ""
            }

    Box(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                    brush =
                                            Brush.linearGradient(
                                                    colors =
                                                            listOf(
                                                                    Color(0xFFF5C563), // Kuning
                                                                    Color(0xFFE8A835) // Orange
                                                            )
                                            )
                            )
                            .clickable(onClick = onClick)
                            .padding(14.dp)
    ) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar (Lebih besar & prominent)
            Box(
                    modifier = Modifier.size(56.dp).clip(CircleShape).background(Color(0xFFFFF9E6)),
                    contentAlignment = Alignment.Center
            ) {
                if (avatarRes != 0) {
                    androidx.compose.foundation.Image(
                            painter = painterResource(avatarRes),
                            contentDescription = contact.name,
                            modifier = Modifier.size(52.dp),
                            contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                            text = contact.name.firstOrNull()?.uppercase() ?: "?",
                            style =
                                    TextStyle(
                                            fontSize = 20.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                            fontWeight = FontWeight(700),
                                            color = Neutral900
                                    )
                    )
                }
            }

            // Chat Content (Name + Last Message)
            Column(
                    modifier = Modifier.weight(1f).padding(top = 2.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Nama Chat
                Text(
                        text = contact.name.ifBlank { "Pengguna" },
                        style =
                                TextStyle(
                                        fontSize = 15.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                        fontWeight = FontWeight(700),
                                        color = Color.White
                                ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                )

                // Last Message Preview
                Text(
                        text = contact.lastMessage.ifBlank { "Mulai percakapan..." },
                        style =
                                TextStyle(
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                        color = Color.White.copy(alpha = 0.90f)
                                ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                )
            }

            // Timestamp & Badge Container
            Column(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Waktu
                Text(
                        text = timeStr,
                        style =
                                TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                        color = Color.White,
                                        fontWeight = FontWeight(500)
                                )
                )

                // Badge Unread Count (Hanya jika ada pesan unread)
                if (contact.unreadCount > 0) {
                    Box(
                            modifier =
                                    Modifier.size(28.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFFF00)),
                            contentAlignment = Alignment.Center
                    ) {
                        Text(
                                text = contact.unreadCount.toString(),
                                style =
                                        TextStyle(
                                                fontSize = 11.sp,
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
}

@Composable
private fun ChatContactCard(contact: ChatContact, onClick: () -> Unit) {
    val context = LocalContext.current
    val avatarRes =
            remember(contact.avatarUrl) {
                if (contact.avatarUrl.isNotBlank())
                        context.resources.getIdentifier(
                                contact.avatarUrl,
                                "drawable",
                                context.packageName
                        )
                else 0
            }

    val timeStr =
            remember(contact.lastMessageMillis) {
                if (contact.lastMessageMillis > 0L)
                        SimpleDateFormat("HH:mm", Locale("id"))
                                .format(Date(contact.lastMessageMillis))
                else ""
            }

    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .clickable(onClick = onClick)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar
        Box(
                modifier =
                        Modifier.size(50.dp)
                                .clip(CircleShape)
                                .background(Primary500)
                                .border(1.dp, Neutral300, CircleShape),
                contentAlignment = Alignment.Center
        ) {
            if (avatarRes != 0) {
                androidx.compose.foundation.Image(
                        painter = painterResource(avatarRes),
                        contentDescription = contact.name,
                        modifier = Modifier.size(46.dp)
                )
            } else {
                Text(
                        text = contact.name.firstOrNull()?.uppercase() ?: "?",
                        style =
                                TextStyle(
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                        color = Neutral900
                                )
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                        text = contact.name.ifBlank { "Pengguna" },
                        style =
                                TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                        fontWeight = FontWeight(600),
                                        color = Neutral900
                                ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                )
                if (timeStr.isNotBlank()) {
                    Text(
                            text = timeStr,
                            style =
                                    TextStyle(
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                            color = Neutral600
                                    )
                    )
                }
            }
            Text(
                    text = contact.lastMessage.ifBlank { "Mulai percakapan..." },
                    style =
                            TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    color =
                                            if (contact.lastMessage.isBlank()) Neutral600
                                            else Neutral700
                            ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
            )
        }
    }

    // Divider
    Box(
            modifier =
                    Modifier.fillMaxWidth()
                            .padding(start = 78.dp)
                            .height(1.dp)
                            .background(Neutral300)
    )
}

@Composable
private fun EmptyChatState() {
    Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Neutral300
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
                text = "Belum ada percakapan",
                style =
                        TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                fontWeight = FontWeight(600),
                                color = Neutral700
                        )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
                text = "Tambahkan teman dan mulai mengobrol!",
                style =
                        TextStyle(
                                fontSize = 13.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = Neutral600
                        ),
                textAlign = TextAlign.Center
        )
    }
}

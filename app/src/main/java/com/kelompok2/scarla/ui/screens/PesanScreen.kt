package com.kelompok2.scarla.ui.screens

// PesanScreen sekarang me-redirect ke ChatPage (chat list)
// Dipanggil dari MainScreen bottom nav "Pesan"
// Route asli: navigasikan ke Screen.Chat.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.kelompok2.scarla.navigation.Screen

/**
 * PesanScreen — wrapper sederhana yang langsung redirect ke ChatPage.
 * Dipertahankan agar routing di MainScreen tidak perlu diubah.
 */
@Composable
fun PesanScreen(navController: NavController? = null) {
    LaunchedEffect(Unit) {
        navController?.navigate(Screen.Chat.route)
    }
    // Jika dipanggil tanpa navController (misal di MainScreen bottom tab),
    // tampilkan langsung ChatPage
    if (navController == null) {
        ChatPage()
    }
}

package com.kelompok2.scarla.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

private data class BottomTab(
    val title: String,
    val drawableName: String,
    val fallbackIcon: ImageVector,
    val badgeCount: Int = 0
)

private val tabs = listOf(
    BottomTab("Home", "home", Icons.Filled.Home),
    BottomTab("Pesan", "pesan", Icons.Filled.Message, badgeCount = 7),
    BottomTab("Cari", "cari", Icons.Filled.Search),
    BottomTab("Belajar", "belajar", Icons.Filled.MenuBook),
    BottomTab("Profil", "profil", Icons.Filled.Person)
)

@Composable
fun MainScreen(navController: NavController? = null) {
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    var showSettingsFromProfile by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            if (index != 4) showSettingsFromProfile = false
                        },
                        icon = {
                            Box {
                                BottomNavIcon(
                                    drawableName = tab.drawableName,
                                    fallbackIcon = tab.fallbackIcon
                                )
                                if (tab.badgeCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = 10.dp, y = (-4).dp)
                                            .size(16.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.error,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = tab.badgeCount.toString(),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onError
                                        )
                                    }
                                }
                            }
                        },
                        label = { Text(text = tab.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> PesanScreen()
                2 -> CariScreen()
                3 -> BelajarScreen()
                4 -> {
                    if (showSettingsFromProfile) {
                        SettingsScreen(
                            navController = navController,
                            onBack = { showSettingsFromProfile = false }
                        )
                    } else {
                        ProfilScreen(
                            navController = navController,
                            onOpenSettings = { showSettingsFromProfile = true }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavIcon(
    drawableName: String,
    fallbackIcon: ImageVector
) {
    val context = LocalContext.current
    val iconRes = remember(drawableName) {
        context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    }

    if (iconRes != 0) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = drawableName,
            modifier = Modifier.size(24.dp)
        )
    } else {
        Icon(
            imageVector = fallbackIcon,
            contentDescription = drawableName
        )
    }
}

package com.kelompok2.scarla.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.kelompok2.scarla.R
import kotlinx.coroutines.delay
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.toMutableStateList
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

data class MateriItem(
    val title: String,
    val duration: String,
    val videoRes: Int,
    var downloaded: Boolean = false,
    var finished: Boolean = false
)

@Composable
fun HtmlScreen(navController: NavController) {

    val context = LocalContext.current

    val downloadedList = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { it.toMutableStateList() }
        )
    ) {
        mutableStateListOf(false, false, false)
    }

    val finishedList = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { it.toMutableStateList() }
        )
    ) {
        mutableStateListOf(false, false, false)
    }

    val materiList = listOf(
        MateriItem(
            "HTML dasar - Pendahuluan",
            "05:20",
            R.raw.html_intro
        ),
        MateriItem(
            "HTML dasar - Tag",
            "08:11",
            R.raw.html_tag
        ),
        MateriItem(
            "HTML dasar - Form",
            "07:42",
            R.raw.html_form
        )
    )

    var selectedVideo by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    var selectedIndex by rememberSaveable {
        mutableStateOf(-1)
    }

    var showDownloadDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showSuccessDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var exoPlayer by remember {
        mutableStateOf<ExoPlayer?>(null)
    }

    val allFinished = finishedList.all { it }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        // HEADER
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(18.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .background(
                            Color(0xFFFFC107),
                            CircleShape
                        )
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "HTML",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // VIDEONYA
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(18.dp)
        ) {

            if (selectedVideo == null) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.ic_html),
                                contentDescription = null,
                                modifier = Modifier.size(90.dp)
                            )
                        }
                    }

            } else {

                DisposableEffect(selectedVideo) {

                    exoPlayer?.release()

                    exoPlayer =
                        ExoPlayer.Builder(context).build().apply {

                            val videoUri = Uri.parse(
                                "android.resource://${context.packageName}/${selectedVideo}"
                            )

                            setMediaItem(MediaItem.fromUri(videoUri))
                            prepare()
                            seekTo(0)
                            playWhenReady = true
                        }

                    onDispose { }
                }

                AndroidView(
                    factory = {
                        PlayerView(context).apply {
                            player = exoPlayer
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                LaunchedEffect(selectedVideo, selectedIndex) {
                    delay(5000)

                    if (selectedIndex != -1) {
                        finishedList[selectedIndex] = true
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Materi",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {

            itemsIndexed(materiList) { index, item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = item.title,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = item.duration,
                                color = Color.Gray
                            )
                        }

                        // DOWNLOAD
                        if (!downloadedList[index]) {

                            IconButton(
                                onClick = {
                                    selectedIndex = index
                                    showDownloadDialog = true
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            enabled = downloadedList[index],
                            onClick = {
                                selectedIndex = index

                                selectedVideo = null
                                selectedVideo = item.videoRes
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    when {
                                        finishedList[index] -> Color(0xFF4CAF50)
                                        downloadedList[index] -> Color(0xFFFFC107)
                                        else -> Color.LightGray
                                    }
                            )
                        ) {

                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                when {
                                    finishedList[index] -> "Finish"
                                    downloadedList[index] -> "Mulai"
                                    else -> "Locked"
                                }
                            )
                        }
                    }
                }
            }

            // QUIZ
            item {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "QUIZ",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "5 soal"
                            )
                        }

                        Button(
                            enabled = allFinished,
                            onClick = {
                                navController.navigate("quiz_html")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    if (allFinished)
                                        Color(0xFFFFC107)
                                    else
                                        Color.LightGray
                            )
                        ) {
                            Text("Mulai")
                        }
                    }
                }
            }
        }
    }

    // DIALOG DOWNLOAD
    if (showDownloadDialog) {

        LaunchedEffect(Unit) {

            delay(2000)

            showDownloadDialog = false
            showSuccessDialog = true

            downloadedList[selectedIndex] = true
        }

        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            title = {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    CircularProgressIndicator()

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Tunggu Sebentar")

                    Text("sedang mengunduh")
                }
            }
        )
    }

    // DIALOG SUKSES
    if (showSuccessDialog) {

        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
            },
            confirmButton = {

                TextButton(
                    onClick = {
                        showSuccessDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            title = {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "SUKSES!",
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )

                    Text("berhasil diunduh")
                }
            }
        )
    }
}

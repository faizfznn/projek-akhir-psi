package com.kelompok2.scarla.ui.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.kelompok2.scarla.ui.theme.*
import com.kelompok2.scarla.ui.components.*
import com.kelompok2.scarla.network.RetrofitClient
import com.kelompok2.scarla.network.MaterialData

@Composable
fun MaterialScreen(
    navController: NavController,
    materialId: String
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    var materialData by remember { mutableStateOf<MaterialData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    var selectedVideo by remember { mutableStateOf<String?>(null) }
    var selectedIndex by remember { mutableStateOf(-1) }

    var showDownloadDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val downloadedList = remember { mutableStateListOf<Boolean>() }
    val finishedList = remember { mutableStateListOf<Boolean>() }

    // State from Firebase
    var isQuizFinished by remember { mutableStateOf(false) }
    var quizFailed by remember { mutableStateOf(false) }
    
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    LaunchedEffect(materialId) {
        try {
            isLoading = true
            val response = RetrofitClient.instance.getMaterial(materialId)
            if (response.success) {
                materialData = response.data
                
                // Initialize lists based on video count
                downloadedList.clear()
                finishedList.clear()
                response.data.videos.forEach { _ ->
                    downloadedList.add(false)
                    finishedList.add(false)
                }

                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    val progressDoc = FirebaseFirestore.getInstance().collection("users").document(uid).collection("materials").document(materialId).get().await()
                    if (progressDoc.exists()) {
                        val vFinished = progressDoc.get("videosFinished") as? List<Boolean>
                        vFinished?.let {
                            for (i in it.indices) {
                                if (i < finishedList.size) finishedList[i] = it[i]
                            }
                        }
                        isQuizFinished = progressDoc.getBoolean("quizPassed") == true
                        quizFailed = progressDoc.getBoolean("quizFailed") == true
                    }
                }
            } else {
                error = response.message ?: "Gagal memuat data materi"
            }
        } catch (e: Exception) {
            error = e.localizedMessage ?: "Terjadi kesalahan"
        } finally {
            isLoading = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (error != null || materialData == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = error ?: "Materi tidak ditemukan")
        }
        return
    }

    val data = materialData!!
    val allFinished = if (finishedList.isNotEmpty()) finishedList.all { it } else true

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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, Neutral200)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.background(Primary500, CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // VIDEO PLAYER
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (selectedVideo == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Neutral50),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_html), // Default fallback icon
                            contentDescription = null,
                            modifier = Modifier.size(90.dp)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    LaunchedEffect(selectedVideo) {
                        selectedVideo?.let { url ->
                            val secureUrl = url.replace("http://", "https://")
                            Log.d("MaterialScreen", "Loading video URI: $secureUrl")
                            exoPlayer.stop()
                            exoPlayer.clearMediaItems()
                            
                            val videoUri = Uri.parse(secureUrl)
                            exoPlayer.setMediaItem(MediaItem.fromUri(videoUri))
                            exoPlayer.prepare()
                            exoPlayer.playWhenReady = true
                        }
                    }

                    key(selectedVideo) {
                        AndroidView(
                            factory = { ctx ->
                                PlayerView(ctx).apply {
                                    player = exoPlayer
                                    useController = true
                                    controllerShowTimeoutMs = 5000
                                }
                            },
                            update = { playerView ->
                                playerView.player = exoPlayer
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                LaunchedEffect(selectedVideo, selectedIndex) {
                    delay(2000)
                    if (selectedIndex != -1 && selectedIndex < finishedList.size) {
                        finishedList[selectedIndex] = true
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        if (uid != null) {
                            FirebaseFirestore.getInstance().collection("users").document(uid)
                                .collection("materials").document(materialId)
                                .set(mapOf("videosFinished" to finishedList.toList()), SetOptions.merge())
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Materi",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            itemsIndexed(data.videos) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.title, fontWeight = FontWeight.SemiBold)
                            Text(text = item.duration, color = Neutral500)
                        }

                        // DOWNLOAD
                        if (index < downloadedList.size && !downloadedList[index]) {
                            IconButton(
                                onClick = {
                                    selectedIndex = index
                                    showDownloadDialog = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null,
                                    tint = Neutral700
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        val isFinished = if (index < finishedList.size) finishedList[index] else false
                        val isDownloaded = if (index < downloadedList.size) downloadedList[index] else false

                        AppButton(
                            text = when {
                                isFinished -> "Selesai"
                                isDownloaded -> "Mulai"
                                else -> "Terkunci"
                            },
                            onClick = {
                                selectedIndex = index
                                selectedVideo = null
                                selectedVideo = item.videoRes
                            },
                            enabled = isDownloaded,
                            modifier = Modifier.wrapContentWidth(),
                            buttonType = when {
                                isFinished -> ButtonType.SUCCESS
                                isDownloaded -> ButtonType.PRIMARY
                                else -> ButtonType.DISABLED
                            }
                        )
                    }
                }
            }

            // QUIZ
            if (data.quizzes.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = data.quizzes[0].title, fontWeight = FontWeight.Bold)
                                Text(text = "${data.quizzes[0].totalQuestions} soal")
                            }

                            val btnText = when {
                                isQuizFinished -> "Selesai"
                                quizFailed -> "Coba Lagi"
                                else -> "Mulai"
                            }
                            val btnType = when {
                                isQuizFinished -> ButtonType.SUCCESS
                                quizFailed -> ButtonType.SECONDARY
                                else -> ButtonType.PRIMARY
                            }

                            AppButton(
                                text = btnText,
                                onClick = {
                                    if (!isQuizFinished) {
                                        navController.navigate("quiz_screen/$materialId/${data.quizzes[0].quizId}")
                                    }
                                },
                                enabled = allFinished,
                                modifier = Modifier.wrapContentWidth(),
                                buttonType = btnType
                            )
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
            if (selectedIndex != -1 && selectedIndex < downloadedList.size) {
                downloadedList[selectedIndex] = true
            }
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
            onDismissRequest = { showSuccessDialog = false },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text(text = "OK", color = Neutral900)
                }
            },
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "SUKSES!", color = Success, style = MaterialTheme.typography.titleLarge)
                    Text("berhasil diunduh")
                }
            }
        )
    }
}

package com.kelompok2.scarla.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kelompok2.scarla.ui.components.*
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.kelompok2.scarla.R
import com.kelompok2.scarla.ui.theme.*
import com.kelompok2.scarla.network.RetrofitClient
import com.kelompok2.scarla.network.QuizData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.FieldValue

@Composable
fun QuizScreen(
    navController: NavController,
    materialId: String,
    quizId: String
) {
    var quizData by remember { mutableStateOf<QuizData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    var currentQuestion by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    
    val answers = remember { mutableStateListOf<String>() }
    val submitted = remember { mutableStateListOf<Boolean>() }
    var isFinished by remember { mutableStateOf(false) }

    LaunchedEffect(quizId) {
        try {
            isLoading = true
            val response = RetrofitClient.instance.getQuiz(quizId)
            if (response.success) {
                quizData = response.data
                
                answers.clear()
                submitted.clear()
                response.data.questions.forEach { _ ->
                    answers.add("")
                    submitted.add(false)
                }
            } else {
                error = response.message ?: "Gagal memuat quiz"
            }
        } catch (e: Exception) {
            error = e.localizedMessage ?: "Terjadi kesalahan"
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (error != null || quizData == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = error ?: "Quiz tidak ditemukan")
        }
        return
    }

    val questions = quizData!!.questions

    if (isFinished) {
        val correctCount = questions.filterIndexed { index, quiz ->
            answers[index] == quiz.correctAnswer
        }.size

        val wrongCount = questions.size - correctCount

        val score = Math.round((correctCount.toFloat() / questions.size) * 100)
        val isPassed = score >= quizData!!.passingScore

        LaunchedEffect(isPassed) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid != null) {
                val firestore = FirebaseFirestore.getInstance()
                val userRef = firestore.collection("users").document(uid)
                
                val materialData = mapOf(
                    "subject" to quizData!!.title,
                    "quizPassed" to isPassed,
                    "quizFailed" to !isPassed,
                    "completedAt" to com.google.firebase.Timestamp.now()
                )
                userRef.collection("materials").document(materialId).set(materialData, SetOptions.merge())

                if (isPassed) {
                    com.kelompok2.scarla.firebase.FirestoreInitializer.recordLessonCompleted(materialId, quizData!!.title)
                }
            }
        }

        ResultScreen(
            correct = correctCount,
            wrong = wrongCount,
            passingScore = quizData!!.passingScore,
            totalQuestions = quizData!!.totalQuestions,
            onBack = {
                navController.popBackStack()
            }
        )
    } else {
        val question = questions[currentQuestion]

        // Sinkronisasi dengan jawaban sebelumnya
        selectedAnswer = answers[currentQuestion]

        val progress = (currentQuestion + 1).toFloat() / questions.size.toFloat()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {

            // HEADER
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, top = 48.dp, end = 18.dp, bottom = 12.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = quizData!!.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 18.dp, end = 18.dp, bottom = 18.dp)
            ) {

            // PROGRESS
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Neutral50)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Soal ${currentQuestion + 1}/${questions.size}",
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "${((progress) * 100).toInt()}%")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = Primary500,
                        trackColor = Neutral200
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // PERTANYAAN DI LUAR CARD
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
            )

            Spacer(modifier = Modifier.height(30.dp))

            // CARD JAWABAN
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Tertiary300),
                border = BorderStroke(2.dp, Primary1000)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Column {
                        question.options.forEach { option ->
                            val isSelected = selectedAnswer == option
                            val isSubmitted = submitted[currentQuestion]
                            val isCorrect = option == question.correctAnswer
                            val isWrong = isSelected && option != question.correctAnswer

                            val backgroundColor = when {
                                isSubmitted && isCorrect -> Success
                                isSubmitted && isWrong -> Error
                                else -> Neutral50
                            }

                            val cardBorder = when {
                                isSubmitted && isCorrect -> BorderStroke(2.dp, Success)
                                isSubmitted && isWrong -> BorderStroke(2.dp, Error)
                                isSelected -> BorderStroke(2.dp, Primary1000)
                                else -> BorderStroke(1.dp, Neutral500)
                            }

                            val elevationValue = if (isSelected && !isSubmitted) 8.dp else 2.dp

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable(enabled = !isSubmitted) {
                                        selectedAnswer = option
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                                border = cardBorder,
                                elevation = CardDefaults.cardElevation(defaultElevation = elevationValue)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = option,
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isSubmitted && (isCorrect || isWrong)) Color.White else Neutral900
                                    )

                                    if (isSubmitted && isCorrect) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Neutral50
                                        )
                                    }

                                    if (isSubmitted && isWrong) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null,
                                            tint = Neutral50
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // SUBMIT BUTTON
                    AppButton(
                        text = "Submit Jawaban",
                        onClick = {
                            answers[currentQuestion] = selectedAnswer
                            submitted[currentQuestion] = true
                        },
                        enabled = selectedAnswer.isNotEmpty() && !submitted[currentQuestion],
                        modifier = Modifier.fillMaxWidth(),
                        buttonType = ButtonType.PRIMARY
                    )

                    if (submitted[currentQuestion]) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Neutral50),
                            border = BorderStroke(1.dp, Primary500)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Penjelasan:",
                                    fontWeight = FontWeight.Bold,
                                    color = Primary1000
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = question.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Neutral900
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // PREV NEXT PALING BAWAH
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppButton(
                    text = "Previous",
                    onClick = {
                        if (currentQuestion > 0) {
                            currentQuestion--
                        }
                    },
                    enabled = currentQuestion != 0,
                    modifier = Modifier.weight(1f),
                    buttonType = ButtonType.SECONDARY
                )

                Spacer(modifier = Modifier.width(12.dp))

                AppButton(
                    text = if (currentQuestion == questions.lastIndex) "Finish" else "Next",
                    onClick = {
                        if (currentQuestion < questions.lastIndex) {
                            currentQuestion++
                        } else {
                            isFinished = true
                        }
                    },
                    enabled = submitted[currentQuestion],
                    modifier = Modifier.weight(1f),
                    buttonType = ButtonType.PRIMARY
                )
            }
        }
    }
}
}

@Composable
fun ResultScreen(correct: Int, wrong: Int, passingScore: Int, totalQuestions: Int, onBack: () -> Unit) {
    val score = Math.round((correct.toFloat() / totalQuestions) * 100)
    val isPassed = score >= passingScore

    val messageTitle = if (isPassed) "Kerja Bagus!" else "Coba Lagi!"
    val resultImage = if (isPassed) R.drawable.ic_excellent else R.drawable.ic_try_again
    val messageDesc = if (isPassed) 
        "Kamu sudah memahami materi dengan sangat baik. Pertahankan semangat belajarmu!" 
    else 
        "Belum ada jawaban yang benar atau nilai masih di bawah standar. Yuk pelajari lagi materinya!"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(start = 24.dp, top = 48.dp, end = 24.dp, bottom = 24.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = resultImage),
                contentDescription = null,
                modifier = Modifier.size(220.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = messageTitle,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = messageDesc,
                style = MaterialTheme.typography.bodyLarge,
                color = Neutral700,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ScoreCard(
                    label = "Benar",
                    value = correct,
                    color = Success,
                    modifier = Modifier.weight(1f)
                )

                ScoreCard(
                    label = "Salah",
                    value = wrong,
                    color = Error,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        AppButton(
            text = "Kembali ke Materi",
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.PRIMARY
        )
    }
}

@Composable
fun ScoreCard(label: String, value: Int, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = BorderStroke(2.dp, color)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontWeight = FontWeight.Medium, color = color)
            Text(text = value.toString(), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

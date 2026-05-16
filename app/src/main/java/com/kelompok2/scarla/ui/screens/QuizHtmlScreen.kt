package com.kelompok2.scarla.ui.screens

import com.kelompok2.scarla.ui.theme.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kelompok2.scarla.ui.components.*
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.kelompok2.scarla.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val answer: String
)

@Composable
fun QuizHtmlScreen(
    navController: NavController,
    onQuizFinished: () -> Unit
) {

    val questions = listOf(

            QuizQuestion(
                "HTML adalah...",
                listOf(
                    "Bahasa pemrograman",
                    "Markup language",
                    "Database",
                    "Framework"
                ),
                "Markup language"
            ),

        QuizQuestion(
            "Tag untuk paragraf adalah...",
            listOf("<p>", "<h1>", "<div>", "<a>"),
            "<p>"
        ),

        QuizQuestion(
            "Tag heading terbesar?",
            listOf("h6", "h4", "h1", "h2"),
            "h1"
        ),

        QuizQuestion(
            "Tag link HTML?",
            listOf("<a>", "<img>", "<p>", "<ul>"),
            "<a>"
        ),

        QuizQuestion(
            "HTML digunakan untuk?",
            listOf(
                "Membuat tampilan web",
                "AI",
                "Database",
                "Game engine"
            ),
            "Membuat tampilan web"
        )
    )

    var currentQuestion by remember {
        mutableStateOf(0)
    }

    // jawaban sementara sebelum submit
    var selectedAnswer by remember {
        mutableStateOf("")
    }

    // jawaban final tiap soal
    val answers = remember {
        mutableStateListOf("", "", "", "", "")
    }

    // status submit tiap soal
    val submitted = remember {
        mutableStateListOf(false, false, false, false, false)
    }

    val question = questions[currentQuestion]

    // kalau pernah jawab sebelumnya
    selectedAnswer = answers[currentQuestion]

    val progress =
        (currentQuestion + 1).toFloat() / questions.size.toFloat()

    var isFinished by remember {
        mutableStateOf(false)
    }

    if (isFinished) {

        val correctCount = questions.filterIndexed { index, quiz ->
            answers[index] == quiz.answer
        }.size

        val wrongCount = questions.size - correctCount

        ResultScreen(
            correct = correctCount,
            wrong = wrongCount,
            onBack = {
                onQuizFinished()
                navController.popBackStack()
            }
        )

    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(18.dp)
        ) {

            // HEADER
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Quiz HTML",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // PROGRESS
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Neutral50                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Soal ${currentQuestion + 1}/${questions.size}",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${((progress) * 100).toInt()}%"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Primary500,
                        trackColor = Neutral200
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // PERTANYAAN DI LUAR CARD
            Text(
                text = question.question,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
            )

            Spacer(modifier = Modifier.height(30.dp))

            // CARD JAWABAN
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Tertiary300
                ),
                border = BorderStroke(2.dp, Primary1000)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Top
                ) {

                    Column {

                        question.options.forEach { option ->

                            val isSelected = selectedAnswer == option
                            val isSubmitted = submitted[currentQuestion]
                            val isCorrect = option == question.answer
                            val isWrong = isSelected && option != question.answer

                            val backgroundColor = when {
                                isSubmitted && isCorrect -> Success
                                isSubmitted && isWrong -> Error
                                else -> Neutral50                            }

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
                                elevation = CardDefaults.cardElevation(defaultElevation = elevationValue) // Menerapkan shadow dinamis
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = option,
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold, // Teks menebal saat dipilih
                                        color = if (isSubmitted && (isCorrect || isWrong)) Color.White else Neutral900                                    )

                                    if (isSubmitted && isCorrect) {

                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Neutral50                                        )
                                    }

                                    if (isSubmitted && isWrong) {

                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null,
                                            tint = Neutral50                                        )
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

                            answers[currentQuestion] =
                                selectedAnswer

                            submitted[currentQuestion] = true
                        },

                        enabled =
                            selectedAnswer.isNotEmpty()
                                    && !submitted[currentQuestion],

                        modifier = Modifier.fillMaxWidth(),

                        buttonType = ButtonType.PRIMARY
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // PREV NEXT PALING BAWAH
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                AppButton(
                    text = "Previous",
                    onClick = {

                        if (currentQuestion > 0) {
                            currentQuestion--
                        }
                    },
                    enabled = currentQuestion != 0,

                    modifier = Modifier
                        .weight(1f),

                    buttonType = ButtonType.SECONDARY
                )

                Spacer(modifier = Modifier.width(12.dp))

                AppButton(

                    text =
                        if (currentQuestion == questions.lastIndex)
                            "Finish"
                        else
                            "Next",

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

@Composable
fun ResultScreen(correct: Int, wrong: Int, onBack: () -> Unit) {

    val messageTitle = when {
        correct >= 4 -> "Kerja Bagus!"
        correct >= 2 -> "Terus Belajar!"
        correct == 1 -> "Jangan Menyerah!"
        else -> "Coba Lagi!"
    }

    val resultImage = when {
        correct >= 4 -> R.drawable.ic_excellent
        correct >= 2 -> R.drawable.ic_keep_learning
        else -> R.drawable.ic_try_again
    }

    val messageDesc = when {
        correct >= 4 ->
            "Kamu sudah memahami materi dengan sangat baik. Pertahankan semangat belajarmu!"

        correct >= 2 ->
            "Hasilmu sudah cukup bagus, tapi masih ada beberapa materi yang perlu dipelajari lagi."

        correct == 1 ->
            "Kamu sudah berusaha dengan baik. Coba ulangi materinya pelan-pelan lalu kerjakan quiz lagi yaa!"

        else ->
            "Belum ada jawaban yang benar. Yuk pelajari lagi materinya dan coba sekali lagi!!"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp)
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResultScreenPreview() {

    ResultScreen(
        correct = 4,
        wrong = 1,
        onBack = {}
    )
}

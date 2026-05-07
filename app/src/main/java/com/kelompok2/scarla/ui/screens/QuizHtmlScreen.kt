package com.kelompok2.scarla.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val answer: String
)

@Composable
fun QuizHtmlScreen(navController: NavController) {

    val questions = listOf(
        QuizQuestion(
            "HTML adalah...",
            listOf("Bahasa pemrograman", "Markup language", "Database", "Framework"),
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
            listOf("Membuat tampilan web", "AI", "Database", "Game engine"),
            "Membuat tampilan web"
        )
    )

    var currentQuestion by remember {
        mutableStateOf(0)
    }

    var selectedAnswer by remember {
        mutableStateOf("")
    }

    var answered by remember {
        mutableStateOf(false)
    }

    val question = questions[currentQuestion]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Soal ${currentQuestion + 1} dari ${questions.size}",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = question.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                question.options.forEach { option ->

                    Button(
                        onClick = {
                            selectedAnswer = option
                            answered = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                when {
                                    !answered -> Color.LightGray
                                    option == question.answer -> Color.Green
                                    option == selectedAnswer -> Color.Red
                                    else -> Color.LightGray
                                }
                        )
                    ) {
                        Text(option)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            enabled = answered,
            onClick = {

                if (currentQuestion < questions.lastIndex) {

                    currentQuestion++
                    answered = false
                    selectedAnswer = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFC107)
            )
        ) {
            Text("Next")
        }
    }
}

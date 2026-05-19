package com.kelompok2.scarla.network

data class ApiResponse<T>(
    val success: Boolean,
    val data: T,
    val message: String? = null
)

data class MaterialData(
    val id: String,
    val title: String,
    val icon: String,
    val description: String,
    val totalVideos: Int,
    val videos: List<VideoData> = emptyList(),
    val quizzes: List<QuizInfo> = emptyList()
)

data class VideoData(
    val id: Int,
    val title: String,
    val duration: String,
    val videoRes: String // URL from backend
)

data class QuizInfo(
    val quizId: String,
    val title: String,
    val totalQuestions: Int
)

data class QuizData(
    val id: String,
    val title: String,
    val duration: Int,
    val totalQuestions: Int,
    val passingScore: Int,
    val questions: List<QuestionData> = emptyList()
)

data class QuestionData(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String
)

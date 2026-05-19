package com.kelompok2.scarla.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ScarlaApiService {
    @GET("api/materials/{materialId}")
    suspend fun getMaterial(@Path("materialId") materialId: String): ApiResponse<MaterialData>

    @GET("api/quizzes/{quizId}")
    suspend fun getQuiz(@Path("quizId") quizId: String): ApiResponse<QuizData>
}

object RetrofitClient {
    private const val BASE_URL = "https://be-scarla.vercel.app/"

    val instance: ScarlaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ScarlaApiService::class.java)
    }
}

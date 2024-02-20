package android.azadev.quizzical.repository

import android.azadev.quizzical.model.DetailedAnswerResult

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/16/2024
 */

interface QuizRepository {

    suspend fun getQuizzesByCategory(categoryId: Int): Result<List<DetailedAnswerResult>>

}
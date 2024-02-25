package android.azadev.quizzical.repository

import android.azadev.quizzical.data.remote.api.QuizApi
import android.azadev.quizzical.data.remote.response.DetailedAnswerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/16/2024
 */

class ProdQuizRepository @Inject constructor(
    private val remoteSource: QuizApi
) : QuizRepository {

    override suspend fun getQuizzesByCategory(categoryId: Int): Result<List<DetailedAnswerResult>> =
        runCatching {
            withContext(Dispatchers.IO) {
                remoteSource.getQuizzesByCategoryId(categoryId = categoryId).results
            }
        }
}
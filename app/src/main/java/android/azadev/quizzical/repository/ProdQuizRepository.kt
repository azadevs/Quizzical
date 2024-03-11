package android.azadev.quizzical.repository

import android.azadev.quizzical.data.local.LocalSource
import android.azadev.quizzical.data.local.entity.ScoreEntity
import android.azadev.quizzical.data.local.entity.UserEntity
import android.azadev.quizzical.data.remote.api.QuizApi
import android.azadev.quizzical.data.remote.response.DetailedAnswerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/16/2024
 */

class ProdQuizRepository @Inject constructor(
    private val remoteSource: QuizApi,
    private val localSource: LocalSource
) : QuizRepository {

    override suspend fun getQuizzesByCategory(categoryId: Int): Result<List<DetailedAnswerResult>> =
        runCatching {
            withContext(Dispatchers.IO) {
                remoteSource.getQuizzesByCategoryId(categoryId = categoryId).results
            }
        }

    override suspend fun insertUserData(userEntity: UserEntity) {
        localSource.insertUserData(userEntity)
    }

    override suspend fun insertScoreData(scoreEntity: ScoreEntity) {
        localSource.insertUserData(scoreEntity)
    }

    override suspend fun updateScoreData(scoreEntity: ScoreEntity) {
        localSource.updateScoreData(scoreEntity)
    }

    override suspend fun updateUserData(userEntity: UserEntity) {
        localSource.updateUserData(userEntity)
    }

    override fun getUserDataById(): Flow<UserEntity> = localSource.getUserDataById()

    override fun getScoreDataById(): Flow<ScoreEntity> = localSource.getScoreDataById()

}
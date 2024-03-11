package android.azadev.quizzical.repository

import android.azadev.quizzical.data.local.entity.ScoreEntity
import android.azadev.quizzical.data.local.entity.UserEntity
import android.azadev.quizzical.data.remote.response.DetailedAnswerResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/16/2024
 */

interface QuizRepository {

    suspend fun getQuizzesByCategory(categoryId: Int): Result<List<DetailedAnswerResult>>

    suspend fun insertUserData(userEntity: UserEntity)

    suspend fun insertScoreData(scoreEntity: ScoreEntity)

    suspend fun updateScoreData(scoreEntity: ScoreEntity)

    suspend fun updateUserData(userEntity: UserEntity)

    fun getUserDataById(): Flow<UserEntity>

    fun getScoreDataById(): Flow<ScoreEntity>
}
package android.azadev.quizzical.data.local

import android.azadev.quizzical.data.local.dao.ScoreDao
import android.azadev.quizzical.data.local.dao.UserDao
import android.azadev.quizzical.data.local.entity.ScoreEntity
import android.azadev.quizzical.data.local.entity.UserEntity
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/10/2024
 */

@Singleton
class LocalSource @Inject constructor(
    private val scoreDao: ScoreDao,
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun insertUserData(userEntity: UserEntity) {
        val userId = userDao.insertUserData(userEntity)
        sharedPreferences.edit().putLong("userId", userId).apply()
    }

    suspend fun insertUserData(scoreEntity: ScoreEntity) {
        val scoreId = scoreDao.insertScoreData(scoreEntity)
        sharedPreferences.edit().putLong("scoreId", scoreId).apply()
    }

    suspend fun updateUserData(userEntity: UserEntity) {
        userDao.updateUserData(userEntity)
    }

    suspend fun updateScoreData(scoreEntity: ScoreEntity) {
        scoreDao.updateScoreData(scoreEntity)
    }
}
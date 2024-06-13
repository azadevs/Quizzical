package android.azadev.quizzical.data.local

import android.azadev.quizzical.data.local.dao.ScoreDao
import android.azadev.quizzical.data.local.dao.UserDao
import android.azadev.quizzical.data.local.entity.ScoreEntity
import android.azadev.quizzical.data.local.entity.UserEntity
import android.azadev.quizzical.utils.Constants.PREFS_SCORE_ID
import android.azadev.quizzical.utils.Constants.PREFS_USER_ID
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
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
        withContext(Dispatchers.IO) {
            val userId = userDao.insertUserData(userEntity)
            sharedPreferences.edit().putLong(PREFS_USER_ID, userId).apply()
        }
    }

    suspend fun insertUserData(scoreEntity: ScoreEntity) {
        withContext(Dispatchers.IO) {
            val scoreId = scoreDao.insertScoreData(scoreEntity)
            sharedPreferences.edit().putLong(PREFS_SCORE_ID, scoreId).apply()
        }
    }

    suspend fun updateUserData(userEntity: UserEntity) {
        withContext(Dispatchers.IO) {
            userDao.updateUserData(userEntity)
        }
    }

    suspend fun updateScoreData(scoreEntity: ScoreEntity) {
        withContext(Dispatchers.IO) {
            scoreDao.updateScoreData(scoreEntity)
        }
    }

    fun getUserDataById() = flow {
        emit(
            userDao.getUserDataById(sharedPreferences.getLong(PREFS_USER_ID, 0))
        )
    }

    fun getScoreDataById() = flow {
        emit(
            scoreDao.getScoreDataById(sharedPreferences.getLong(PREFS_SCORE_ID, 0))
        )
    }

    fun hasUserData() = userDao.hasUserData()

    fun hasScoreData() = scoreDao.hasScoreData()

}
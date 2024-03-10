package android.azadev.quizzical.data.local

import android.azadev.quizzical.data.local.dao.ScoreDao
import android.azadev.quizzical.data.local.dao.UserDao
import android.azadev.quizzical.data.local.entity.ScoreEntity
import android.azadev.quizzical.data.local.entity.UserEntity
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/9/2024
 */

@Database(entities = [UserEntity::class, ScoreEntity::class], version = 1, exportSchema = false)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun scoreDao(): ScoreDao
}
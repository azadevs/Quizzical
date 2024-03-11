package android.azadev.quizzical.data.local.dao

import android.azadev.quizzical.data.local.entity.ScoreEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/8/2024
 */

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScoreData(scoreEntity: ScoreEntity): Long

    @Update
    suspend fun updateScoreData(scoreEntity: ScoreEntity)

}
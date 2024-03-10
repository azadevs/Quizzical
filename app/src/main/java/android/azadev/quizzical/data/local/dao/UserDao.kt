package android.azadev.quizzical.data.local.dao

import android.azadev.quizzical.data.local.entity.UserEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/8/2024
 */

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(userEntity: UserEntity): Long

    @Update
    suspend fun updateUserData(userEntity: UserEntity)

}
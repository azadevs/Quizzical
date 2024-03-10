package android.azadev.quizzical.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/8/2024
 */

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("first_name")
    val fName: String,
    @ColumnInfo("last_name")
    val lName: String,
    @ColumnInfo("user_image")
    val userImageUri: String,
    @ColumnInfo("date_of_birth")
    val dateOfBirth: String,
    val email: String,

    )

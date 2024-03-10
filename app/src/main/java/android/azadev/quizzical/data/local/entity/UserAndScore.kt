package android.azadev.quizzical.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/9/2024
 */

data class UserAndScore(
    @Embedded val userEntity: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val scoreEntity: ScoreEntity
)

package android.azadev.quizzical.ui.game.model

import java.io.Serializable

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/11/2024
 */

data class GameScoreData(
    val correctAnswersCount: Int,
    val incorrectAnswersCount: Int,
    val category: String
) : Serializable

package android.azadev.quizzical.ui.game.model

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/18/2024
 */

sealed interface GameState<out T> {
    data object Loading : GameState<Nothing>
    data class Error(val message: String) : GameState<Nothing>
    data class Success<T>(val data: T) : GameState<T>
    data object GameOver : GameState<Nothing>
}
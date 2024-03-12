package android.azadev.quizzical.ui.leaderboard.viewmodel

import android.azadev.quizzical.data.local.entity.UserAndScore
import android.azadev.quizzical.repository.QuizRepository
import android.azadev.quizzical.utils.Constants.MONTH_MILLISECONDS
import android.azadev.quizzical.utils.Constants.WEEK_MILLISECONDS
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/11/2024
 */

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _usersAndScoresFlow = MutableStateFlow<List<UserAndScore>>(emptyList())
    val userAndScore = _usersAndScoresFlow.asStateFlow()

    fun getAllUsers(category: String) {
        viewModelScope.launch(IO) {
            val filteredFlow = when (category) {
                "All time" -> repository.getUserAndScores()
                "This week" -> repository.getUserAndScores().map { list ->
                    list.filter { userAndScore ->
                        userAndScore.scoreEntity.date + WEEK_MILLISECONDS >= System.currentTimeMillis()
                    }
                }

                "Month" -> repository.getUserAndScores().map { list ->
                    list.filter { userAndScore ->
                        userAndScore.scoreEntity.date + MONTH_MILLISECONDS >= System.currentTimeMillis()
                    }
                }

                else -> {
                    throw IllegalArgumentException("Invalid category: $category")
                }
            }
            filteredFlow
                .onEach { _usersAndScoresFlow.value = it }
                .launchIn(viewModelScope)
        }
    }
}

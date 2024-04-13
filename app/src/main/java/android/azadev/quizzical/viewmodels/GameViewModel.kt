package android.azadev.quizzical.viewmodels

import android.azadev.quizzical.data.local.entity.ScoreEntity
import android.azadev.quizzical.data.remote.response.DetailedAnswerResult
import android.azadev.quizzical.repository.QuizRepository
import android.azadev.quizzical.utils.Categories
import android.azadev.quizzical.utils.Constants
import android.azadev.quizzical.utils.Constants.QUESTIONS_AMOUNT
import android.azadev.quizzical.utils.Constants.TOTAL_SECONDS
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/16/2024
 */

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: QuizRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var timerJob: Job? = null

    private var _timerSharedFlow = MutableSharedFlow<Int>()
    val timerSharedFlow = _timerSharedFlow.asSharedFlow()

    private var _gameState =
        MutableStateFlow<GameState<List<DetailedAnswerResult>>>(GameState.Loading)
    val uiState = _gameState.asStateFlow()

    private var _currentQuestionPosition = MutableStateFlow(0)
    val currentQuestionPosition = _currentQuestionPosition.asStateFlow()

    private var _correctProgressFlow = MutableStateFlow(0)
    val correctProgressFlow = _correctProgressFlow.asStateFlow()

    private var _incorrectProgressFlow = MutableStateFlow(0)
    val incorrectProgressFlow = _incorrectProgressFlow.asStateFlow()

    private val quizzes = mutableListOf<DetailedAnswerResult>()

    init {
        val categoryId = when (savedStateHandle.get<String>("category")) {
            Categories.Geography.name -> Constants.GEOGRAPHY_ID
            Categories.Math.name -> Constants.MATH_ID
            Categories.Sport.name -> Constants.SPORT_ID
            Categories.History.name -> Constants.HISTORY_ID
            else -> Constants.GENERAL_KNOWLEDGE
        }
        getQuizzesByCategoryId(categoryId)
    }

    private fun getQuizzesByCategoryId(categoryId: Int) {
        viewModelScope.launch {
            repository.getQuizzesByCategory(categoryId)
                .onSuccess {
                    _gameState.value = GameState.Success(it)
                    quizzes.addAll(it)
                    startTimerIfSuccess()
                }
                .onFailure {
                    _gameState.value = GameState.Error(it.localizedMessage ?: "Error occurred")
                }
        }
    }

    fun submitAnswer(selectedAnswer: String = "unSelected") {
        if (isCorrectAnswer(selectedAnswer)) {
            _correctProgressFlow.value += 1
        } else {
            _incorrectProgressFlow.value += 1
        }
    }

    private fun isCorrectAnswer(selectedAnswer: String) =
        quizzes[currentQuestionPosition.value].correctAnswer == selectedAnswer

    private fun startTimerIfSuccess() {
        timerJob = viewModelScope.launch {
            initTimer()
                .onCompletion {
                    _timerSharedFlow.emit(-1)
                    nextQuestionAndRestartTimer()
                }
                .collect {
                    _timerSharedFlow.emit(it)
                }
        }
    }

    fun nextQuestionAndRestartTimer() {
        if (_currentQuestionPosition.value >= QUESTIONS_AMOUNT - 1) {
            stopTimer()
            _gameState.value = GameState.GameOver
        } else {
            _currentQuestionPosition.update {
                it + 1
            }
            restartTimer()
        }
    }

    private fun restartTimer() {
        stopTimer()
        startTimerIfSuccess()
    }

    private fun initTimer(): Flow<Int> =
        (TOTAL_SECONDS - 1 downTo 0).asFlow()
            .onEach { delay(1000) }
            .onStart {
                emit(TOTAL_SECONDS)
            }.conflate()

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun upsertScoreData(scoreEntity: ScoreEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (repository.hasScoreData()) {
                    val existingScore = repository.getScoreDataById().firstOrNull()
                    val updatedScore = existingScore?.let {
                        ScoreEntity(
                            score = it.score + scoreEntity.score,
                            userId = scoreEntity.userId,
                            date = scoreEntity.date,
                            id = it.id
                        )
                    } ?: scoreEntity
                    repository.updateScoreData(updatedScore)
                } else {
                    repository.insertScoreData(scoreEntity)
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "Error inserting score data", e)
            }
        }
    }

}

sealed interface GameState<out T> {
    data object Loading : GameState<Nothing>
    data class Error(val message: String) : GameState<Nothing>
    data class Success<T>(val data: T) : GameState<T>
    data object GameOver : GameState<Nothing>
}


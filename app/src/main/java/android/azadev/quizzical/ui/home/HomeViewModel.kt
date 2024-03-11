package android.azadev.quizzical.ui.home

import android.azadev.quizzical.repository.QuizRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/10/2024
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: QuizRepository
) : ViewModel() {

    val scoreFlow = repository.getScoreDataById()

}
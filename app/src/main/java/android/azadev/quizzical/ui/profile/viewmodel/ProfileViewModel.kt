package android.azadev.quizzical.ui.profile.viewmodel

import android.azadev.quizzical.data.local.entity.UserEntity
import android.azadev.quizzical.repository.QuizRepository
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/10/2024
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: QuizRepository,
    private val preferences: SharedPreferences
) : ViewModel() {

    val user = repository.getUserDataById()

    fun insertUserData(userEntity: UserEntity) {
        viewModelScope.launch {
            repository.insertUserData(userEntity)
        }
    }

    fun updateUserData(userEntity: UserEntity) {
        viewModelScope.launch {
            repository.updateUserData(userEntity)
        }
    }

}
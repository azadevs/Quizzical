package android.azadev.quizzical.ui.game

import android.annotation.SuppressLint
import android.azadev.quizzical.R
import android.azadev.quizzical.data.local.entity.ScoreEntity
import android.azadev.quizzical.data.remote.response.DetailedAnswerResult
import android.azadev.quizzical.databinding.FragmentGameBinding
import android.azadev.quizzical.utils.Constants
import android.azadev.quizzical.utils.UIExtensions.inVisible
import android.azadev.quizzical.utils.UIExtensions.visible
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/14/2024
 */

@AndroidEntryPoint
class GameFragment : Fragment(R.layout.fragment_game) {

    private var _binding: FragmentGameBinding? = null

    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()

    private var correctAnswerCount: Int = 0

    private var incorrectAnswerCount: Int = 0

    private val navArgs: GameFragmentArgs by navArgs()

    @Inject
    lateinit var preferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        _binding = FragmentGameBinding.bind(view)

        var isChecked = false

        binding.progressBarTimer.maxProgress = Constants.TOTAL_SECONDS.toDouble()

        binding.ivCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1 && !isChecked) {
                val checkedRadioButtonId =
                    requireView().findViewById<RadioButton>(checkedId).text.toString()
                viewModel.submitAnswer(checkedRadioButtonId)
                radioButtonDisabled()
                isChecked = true
            }
        }

        binding.btnNext.setOnClickListener {
            if (!isChecked) {
                Snackbar.make(binding.root, "Please choose one option", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                binding.radioGroup.clearCheck()
                setDefaultBackgroundToRadioButton()
                viewModel.nextQuestionAndRestartTimer()
                isChecked = false
            }
        }

        viewModel.correctProgressFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { value ->
                updateCorrectProgressbar(value)
                correctAnswerCount = value
                correctAnswerBackground()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.incorrectProgressFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { value ->
                updateIncorrectProgressbar(value)
                incorrectAnswerCount = value
                inCorrectAnswerBackground()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { result ->
            when (result) {
                is GameState.Error -> showErrorSnackbar(result.message)
                GameState.Loading -> showLoadingProgressbar()
                is GameState.Success -> configureUI(result.data)
                GameState.GameOver -> gameOver()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.timerSharedFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { currentTime ->
                binding.progressBarTimer.setCurrentProgress(currentTime.toDouble())
                if (currentTime == 0) {
                    setDefaultBackgroundToRadioButton()
                    binding.radioGroup.clearCheck()
                    if (!isChecked) {
                        viewModel.submitAnswer("")
                    }
                    isChecked = false
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun inCorrectAnswerBackground() {
        if (getCheckedRadioButtonId() != -1) {
            val selectedRadioButton =
                requireView().findViewById<RadioButton>(getCheckedRadioButtonId())
            selectedRadioButton.setBackgroundResource(R.drawable.incorrect_radio_button)
        }
    }

    private fun correctAnswerBackground() {
        if (getCheckedRadioButtonId() != -1) {
            val selectedRadioButton =
                requireView().findViewById<RadioButton>(getCheckedRadioButtonId())
            selectedRadioButton.setBackgroundResource(R.drawable.correct_radio_button)
        }
    }

    private fun updateIncorrectProgressbar(progress: Int) {
        binding.inCorrectProgressBar.progress = progress
        binding.countIncorrectTv.text =
            if (progress / 10 == 0) "0$progress" else progress.toString()
    }

    private fun updateCorrectProgressbar(progress: Int) {
        binding.correctProgressBar.progress = progress
        binding.countCorrectTv.text = if (progress / 10 == 0) "0$progress" else progress.toString()
    }

    private fun showErrorSnackbar(message: String) {
        hideProgressbar()
        Snackbar.make(
            requireView(), message, Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showLoadingProgressbar() {
        showProgressbar()
    }

    private fun gameOver() {
        viewModel.insertScoreData(
            ScoreEntity(
                score = correctAnswerCount,
                userId = preferences.getLong(Constants.PREFS_USER_ID, 0).toInt(),
                date = System.currentTimeMillis()
            )
        )
        binding.progressBarTimer.setCurrentProgress(0.0)
        val gameScoreData = GameScoreData(
            correctAnswersCount = correctAnswerCount,
            incorrectAnswersCount = incorrectAnswerCount,
            category = navArgs.category
        )
        val action =
            GameFragmentDirections.actionGameFragmentToGameOverFragmentDialog(gameScoreData)
        findNavController().navigate(action)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun configureUI(quizzes: List<DetailedAnswerResult>) {
        hideProgressbar()
        viewModel.currentQuestionPosition.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { count ->
                binding.apply {
                    val data = quizzes[count]
                    tvQuestion.text = data.question
                    tvQuestionCount.text = "${(count + 1)}/${quizzes.size}"
                    binding.linearProgressbar.setProgress((count + 1) * 10, true)
                    configureRadioButtons(data)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun configureRadioButtons(data: DetailedAnswerResult) {
        val variants = data.incorrectAnswers + data.correctAnswer
        val shuffled = variants.shuffled()
        binding.apply {
            if (shuffled.size == 2) {
                rbThirdVariant.inVisible()
                rbFourthVariant.inVisible()
                rbFirstVariant.text = shuffled[0]
                rbSecondVariant.text = shuffled[1]
            } else {
                rbThirdVariant.visible()
                rbFourthVariant.visible()
                rbFirstVariant.text = shuffled[0]
                rbSecondVariant.text = shuffled[1]
                rbThirdVariant.text = shuffled[2]
                rbFourthVariant.text = shuffled[3]
            }
        }
    }

    private fun getCheckedRadioButtonId() = binding.radioGroup.checkedRadioButtonId


    private fun radioButtonEnabled() {
        binding.radioGroup.children.forEach { radioButton ->
            radioButton.isEnabled = true
        }
    }

    private fun radioButtonDisabled() {
        binding.radioGroup.children.forEach { radioButton ->
            radioButton.isEnabled = false
        }
    }

    private fun setDefaultBackgroundToRadioButton() {
        radioButtonEnabled()
        binding.radioGroup.children.forEach { radioButton ->
            radioButton.setBackgroundResource(R.drawable.radio_button_background)
        }
    }

    private fun showProgressbar() {
        binding.frame.visible()
        binding.progressbar.visible()
    }

    private fun hideProgressbar() {
        binding.frame.inVisible()
        binding.progressbar.inVisible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package android.azadev.quizzical.ui.home

import android.azadev.quizzical.R
import android.azadev.quizzical.data.local.entity.ScoreEntity
import android.azadev.quizzical.databinding.FragmentHomeBinding
import android.azadev.quizzical.utils.Categories
import android.azadev.quizzical.utils.Constants
import android.azadev.quizzical.utils.isNetworkAvailable
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/10/2024
 */

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnClickListener {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bool = preferences.getBoolean(Constants.PREFS_IS_HAVE, false)
        if (!bool) {
            findNavController().navigate(R.id.action_homeFragment_to_createProfileFragment)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.ivProfile.setOnClickListener(this)

        binding.cardGeography.setOnClickListener(this)

        binding.cardHistory.setOnClickListener(this)

        binding.cardMath.setOnClickListener(this)

        binding.cardSport.setOnClickListener(this)


        changeToolbarTitle()

        viewModel.scoreFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .filterNotNull()
            .distinctUntilChanged()
            .onEach { score ->
                configureScoreCard(score)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun changeToolbarTitle() {
        val userName = preferences.getString(Constants.PREFS_USER_NAME, "NoName")
        binding.toolbar.title = getString(R.string.text_toolbar_name, userName)
    }

    private fun configureScoreCard(score: ScoreEntity) {
        if (score.score != 0) {
            binding.tvPointCount.text =
                if (score.score < 10) "0${score.score}" else score.score.toString()
        }
    }

    private fun navigateToGameScreen(category: String) {
        if (isNetworkAvailable(requireContext())) {
            val actionHomeFragmentToGameFragment =
                HomeFragmentDirections.actionHomeFragmentToGameFragment(category)
            findNavController().navigate(actionHomeFragmentToGameFragment)
        } else {
            Snackbar.make(
                binding.root,
                getString(R.string.error_network_connection),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card_geography -> {
                navigateToGameScreen(Categories.Geography.name)
            }

            R.id.card_history -> {
                navigateToGameScreen(Categories.History.name)
            }

            R.id.card_sport -> {
                navigateToGameScreen(Categories.Sport.name)
            }

            R.id.card_math -> {
                navigateToGameScreen(Categories.Math.name)
            }

            R.id.iv_profile -> {
                findNavController().navigate(R.id.action_homeFragment_to_editProfileFragment)
            }
        }
    }
}
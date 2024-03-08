package android.azadev.quizzical.ui.home

import android.azadev.quizzical.R
import android.azadev.quizzical.databinding.FragmentHomeBinding
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/10/2024
 */

class HomeFragment : Fragment(R.layout.fragment_home), OnClickListener {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        _binding = FragmentHomeBinding.bind(view)

        binding.ivProfile.setOnClickListener(this)

        binding.cardGeography.setOnClickListener(this)

        binding.cardHistory.setOnClickListener(this)

        binding.cardMath.setOnClickListener(this)

        binding.cardSport.setOnClickListener(this)
    }

    private fun navigateToGameScreen() {
        findNavController().navigate(R.id.action_homeFragment_to_gameFragment)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card_geography -> {
                navigateToGameScreen()
            }

            R.id.card_history -> {
                navigateToGameScreen()
            }

            R.id.card_history -> {
                navigateToGameScreen()
            }

            R.id.card_sport -> {
                navigateToGameScreen()
            }

            R.id.iv_profile -> {
            }
        }
    }
}
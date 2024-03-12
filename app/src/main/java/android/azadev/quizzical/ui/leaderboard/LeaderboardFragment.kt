package android.azadev.quizzical.ui.leaderboard

import android.azadev.quizzical.R
import android.azadev.quizzical.databinding.FragmentLeaderboardBinding
import android.azadev.quizzical.ui.leaderboard.adapter.LeaderboardPagerAdapter
import android.azadev.quizzical.utils.Constants
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/7/2024
 */

@AndroidEntryPoint
class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard) {
    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: LeaderboardPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentLeaderboardBinding.bind(view)

        configureViewpagerWithTab()

    }

    private fun configureViewpagerWithTab() {
        pagerAdapter = LeaderboardPagerAdapter(this, Constants.getAllTabsNames())
        binding.viewpager.adapter = pagerAdapter
        binding.apply {
            TabLayoutMediator(tab, viewpager) { tab, position ->
                tab.text = Constants.getAllTabsNames()[position]
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
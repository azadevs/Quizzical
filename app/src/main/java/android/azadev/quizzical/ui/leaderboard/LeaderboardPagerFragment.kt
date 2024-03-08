package android.azadev.quizzical.ui.leaderboard

import android.azadev.quizzical.R
import android.azadev.quizzical.databinding.FragmentLeaderboardPagerBinding
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/7/2024
 */

class LeaderboardPagerFragment : Fragment(R.layout.fragment_leaderboard_pager) {
    private var _binding: FragmentLeaderboardPagerBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLeaderboardPagerBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): Fragment {
            val fragment = LeaderboardPagerFragment()
            return fragment
        }
    }

}
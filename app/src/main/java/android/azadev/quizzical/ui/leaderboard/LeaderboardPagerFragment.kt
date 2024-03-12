package android.azadev.quizzical.ui.leaderboard

import android.azadev.quizzical.R
import android.azadev.quizzical.data.local.entity.UserAndScore
import android.azadev.quizzical.databinding.FragmentLeaderboardPagerBinding
import android.azadev.quizzical.ui.leaderboard.adapter.LeaderboardUserAdapter
import android.azadev.quizzical.ui.leaderboard.viewmodel.LeaderboardViewModel
import android.azadev.quizzical.utils.UIExtensions.inVisible
import android.azadev.quizzical.utils.UIExtensions.visible
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/7/2024
 */

@AndroidEntryPoint
class LeaderboardPagerFragment : Fragment(R.layout.fragment_leaderboard_pager) {
    private var _binding: FragmentLeaderboardPagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: LeaderboardUserAdapter

    private val viewModel: LeaderboardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLeaderboardPagerBinding.bind(view)

        val category = arguments?.getString(CATEGORY_NAME)

        viewModel.userAndScore
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .distinctUntilChanged()
            .onEach { users ->
                configureUserAdapter(users)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        category?.let { viewModel.getAllUsers(it) }

    }

    private fun configureUserAdapter(userAndScores: List<UserAndScore>) {
        if (userAndScores.isEmpty()) {
            binding.tvNoData.visible()
        } else {
            binding.tvNoData.inVisible()
            adapter = LeaderboardUserAdapter(userAndScores)
            binding.rvUser.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CATEGORY_NAME = "category"
        fun newInstance(category: String): Fragment {
            val fragment = LeaderboardPagerFragment()
            fragment.arguments = bundleOf(CATEGORY_NAME to category)
            return fragment
        }
    }

}
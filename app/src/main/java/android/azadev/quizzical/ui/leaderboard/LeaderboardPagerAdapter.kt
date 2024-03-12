package android.azadev.quizzical.ui.leaderboard

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/7/2024
 */

class LeaderboardPagerAdapter(fragment: Fragment, private val categories: List<String>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return LeaderboardPagerFragment.newInstance(categories[position])
    }
}
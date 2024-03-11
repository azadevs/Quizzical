package android.azadev.quizzical.ui

import android.azadev.quizzical.R
import android.azadev.quizzical.databinding.ActivityQuizBinding
import android.azadev.quizzical.utils.UIExtensions.inVisible
import android.azadev.quizzical.utils.UIExtensions.visible
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var listener: NavController.OnDestinationChangedListener
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.fragmentContainerView)

        configureBottomBar(navController)

        listener = NavController.OnDestinationChangedListener { _, desitntaion, _ ->
            when (desitntaion.id) {
                R.id.homeFragment -> binding.bottomBar.visible()
                R.id.leaderboardFragment -> binding.bottomBar.visible()
                else -> binding.bottomBar.inVisible()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(listener)
    }

    private fun configureBottomBar(navController: NavController) {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_nav)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }
}
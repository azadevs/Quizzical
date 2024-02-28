package android.azadev.quizzical.ui.game

import android.azadev.quizzical.R
import android.azadev.quizzical.databinding.DialogScoreBinding
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/25/2024
 */

class ScoreDialogFragment : DialogFragment(R.layout.dialog_score) {

    private var _binding: DialogScoreBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = DialogScoreBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
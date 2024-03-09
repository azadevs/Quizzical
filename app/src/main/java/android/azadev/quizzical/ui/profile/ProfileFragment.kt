package android.azadev.quizzical.ui.profile

import android.azadev.quizzical.R
import android.azadev.quizzical.databinding.FragmentProfileBinding
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/8/2024
 */

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
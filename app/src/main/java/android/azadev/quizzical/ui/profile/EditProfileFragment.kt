package android.azadev.quizzical.ui.profile

import android.app.Activity
import android.azadev.quizzical.R
import android.azadev.quizzical.data.local.entity.ScoreEntity
import android.azadev.quizzical.data.local.entity.UserEntity
import android.azadev.quizzical.databinding.FragmentEditProfileBinding
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/8/2024
 */

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)

        binding.btnSave.setOnClickListener {
            saveUserWithScoreDataToDatabase()
            findNavController().popBackStack()
        }

        binding.ivSubtract.setOnClickListener {
            val img = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            selectImageFromGallery.launch(img)
        }
    }

    private val selectImageFromGallery =
        registerForActivityResult(
            StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                imageUri = data?.data
                if (imageUri != null) {
                    Glide.with(binding.root).load(imageUri).into(binding.ivUserImage)
                } else {
                    binding.ivUserImage.setImageResource(R.drawable.placeholder)
                }
            }
        }

    private fun saveUserWithScoreDataToDatabase() {
        val firstname = binding.edtFname.text.toString()
        val email = binding.edtEmail.text.toString()
        val lastname = binding.edtLname.text.toString()

        val user = UserEntity(
            fName = firstname,
            userImageUri = imageUri.toString(),
            lName = lastname,
            email = email,
            dateOfBirth = ""
        )
        viewModel.upsertUserData(user)
        viewModel.upsertScoreData(
            ScoreEntity(score = 0, userId = user.id)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        imageUri = null
    }
}
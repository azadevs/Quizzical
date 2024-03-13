package android.azadev.quizzical.ui.profile.edit

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.azadev.quizzical.BuildConfig
import android.azadev.quizzical.R
import android.azadev.quizzical.data.local.entity.UserEntity
import android.azadev.quizzical.databinding.FragmentEditProfileBinding
import android.azadev.quizzical.ui.profile.viewmodel.ProfileViewModel
import android.azadev.quizzical.utils.Constants
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Calendar
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/10/2024
 */

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private var imageUri: Uri? = null

    private lateinit var datePickerDialog: DatePickerDialog

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)

        viewModel.user.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .distinctUntilChanged()
            .onEach { user ->
                configureUi(user)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.ivSubtract.setOnClickListener {
            checkReadExternalStoragePermission()
        }

        configureDatePickerDialog()

        binding.btnSave.setOnClickListener {
            updateAndSaveUserData()

        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun configureUi(user: UserEntity) {
        if (!TextUtils.isEmpty(user.userImageUri)) {
            Glide.with(binding.root).load(user.userImageUri).into(binding.ivUserImage)
        } else {
            binding.ivUserImage.setImageResource(R.drawable.placeholder)
        }
        binding.edtFname.setText(user.fName)
        binding.edtLname.setText(user.lName)
        binding.edtEmail.setText(user.email)
        binding.edtDateBirth.setText(user.dateOfBirth)
    }

    private fun updateAndSaveUserData() {
        val firstname = binding.edtFname.text.toString()
        val email = binding.edtEmail.text.toString()
        val lastname = binding.edtLname.text.toString()
        val dateOfBirth = binding.edtDateBirth.text.toString()

        if (TextUtils.isEmpty(firstname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(lastname) || TextUtils.isEmpty(
                dateOfBirth
            )
        ) {
            Snackbar.make(binding.root, "Please, fill in all fields", Snackbar.LENGTH_SHORT).show()
        } else {
            val user = UserEntity(
                fName = firstname,
                userImageUri = if (imageUri != null) imageUri.toString() else "",
                lName = lastname,
                email = email,
                dateOfBirth = dateOfBirth,
                id = preferences.getLong(Constants.PREFS_USER_ID, 0).toInt()
            )
            viewModel.updateUserData(user)
            preferences.edit().putString(Constants.PREFS_USER_NAME, user.fName).apply()
            Snackbar.make(binding.root, "Updated your data", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private val selectImageFromGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
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

    private fun configureDatePickerDialog() {
        val onDateSetListener = DatePickerDialog.OnDateSetListener { view, y, m, d ->
            binding.edtDateBirth.setText(
                Constants.makeDateToString(
                    day = d, month = m + 1, year = y, requireContext()
                )
            )
        }
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(requireContext(), onDateSetListener, year, month, day)
    }

    private fun checkReadExternalStoragePermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                val img = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                selectImageFromGallery.launch(img)
            }

            shouldShowRequestPermissionRationale(permission) -> {
                Snackbar.make(
                    binding.root,
                    R.string.storage_permission_required,
                    Snackbar.LENGTH_SHORT
                ).setAction(
                    R.string.go_to_settings
                ) {
                    val uri: Uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        data = uri
                        startActivity(this)
                    }
                }.show()
            }

            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val img = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            selectImageFromGallery.launch(img)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        imageUri = null
    }
}
package io.aethibo.fireshare.features.settings.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.transform.CircleCropTransformation
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.entities.ProfileUpdate
import io.aethibo.fireshare.core.utils.EventObserver
import io.aethibo.fireshare.core.utils.FirebaseUtil.auth
import io.aethibo.fireshare.databinding.FragmentSettingsBinding
import io.aethibo.fireshare.features.settings.viewmodel.SettingsViewModel
import io.aethibo.fireshare.features.utils.slideUpViews
import io.aethibo.fireshare.features.utils.snackBar
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(R.layout.fragment_settings), View.OnClickListener {

    private val binding: FragmentSettingsBinding by viewBinding()

    private val viewModel: SettingsViewModel by viewModel()

    private var curImageUri: Uri? = null

    private lateinit var uid: String

    private lateinit var cropContent: ActivityResultLauncher<Any?>

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {

        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                    .setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .getIntent(requireContext())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cropContent = registerForActivityResult(cropActivityResultContract) { uri ->
            uri?.let {
                viewModel.setCurrentImageUri(it)
                binding.btnUpdateProfile.isEnabled = true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uid = auth.uid!!
        viewModel.getUser(uid)

        subscribeToObservers()
        setButtonClickListeners()
        setViewsEnable()

        slideUpViews(
                requireContext(),
                binding.ivProfileImage,
                binding.etFullName,
                binding.etUsername,
                binding.etBio,
                binding.btnUpdateProfile
        )
    }

    private fun setViewsEnable() {
        binding.btnUpdateProfile.isEnabled = false

        binding.etFullName.addTextChangedListener {
            binding.btnUpdateProfile.isEnabled = true
        }

        binding.etUsername.addTextChangedListener {
            binding.btnUpdateProfile.isEnabled = true
        }

        binding.etBio.addTextChangedListener {
            binding.btnUpdateProfile.isEnabled = true
        }
    }

    private fun setButtonClickListeners() {
        binding.btnUpdateProfile.setOnClickListener(this)
        binding.ivProfileImage.setOnClickListener(this)
    }

    private fun subscribeToObservers() {

        viewModel.getUserStatus.observe(viewLifecycleOwner, EventObserver(
                onLoading = {
                    binding.settingsProgressBar.isVisible = true
                },
                onSuccess = { user ->
                    binding.settingsProgressBar.isVisible = false

                    binding.ivProfileImage.load(user.photoUrl) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                    }

                    binding.etFullName.setText(user.displayName)
                    binding.etUsername.setText(user.username)
                    binding.etBio.setText(user.bio)

                    binding.btnUpdateProfile.isEnabled = false
                },
                onError = {
                    binding.settingsProgressBar.isVisible = false
                    snackBar(it)
                }
        ))

        viewModel.curImageUri.observe(viewLifecycleOwner) { uri ->
            curImageUri = uri
            binding.ivProfileImage.load(uri) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }

        viewModel.updateProfileStatus.observe(viewLifecycleOwner, EventObserver(
                onLoading = {
                    binding.settingsProgressBar.isVisible = true
                    binding.btnUpdateProfile.isEnabled = false
                },
                onSuccess = {
                    binding.settingsProgressBar.isVisible = false
                    binding.btnUpdateProfile.isEnabled = false
                    snackBar(requireContext().getString(R.string.profile_updated))
                },
                onError = {
                    binding.settingsProgressBar.isVisible = false
                    snackBar(it)
                    binding.btnUpdateProfile.isEnabled = true
                }
        ))
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivProfileImage -> cropContent.launch(null)
            R.id.btnUpdateProfile -> {
                val fullName = binding.etFullName.text?.trim().toString()
                val username = binding.etUsername.text?.trim().toString()
                val bio = binding.etBio.text?.trim().toString()
                val profileUpdate = ProfileUpdate(
                        uid,
                        fullName,
                        username,
                        bio,
                        curImageUri
                )

                viewModel.updateProfile(profileUpdate)
            }
        }
    }
}
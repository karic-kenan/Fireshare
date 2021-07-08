/*
 * Created by Karic Kenan on 8.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.settings.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.transform.CircleCropTransformation
import com.github.dhaval2404.imagepicker.ImagePicker
import io.aethibo.fireshare.R
import io.aethibo.fireshare.databinding.FragmentSettingsBinding
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.domain.request.ProfileUpdateRequestBody
import io.aethibo.fireshare.framework.utils.FirebaseUtil
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.settings.viewmodel.SettingsViewModel
import io.aethibo.fireshare.ui.utils.snackBar
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SettingsFragment : Fragment(R.layout.fragment_settings), View.OnClickListener {

    private val binding: FragmentSettingsBinding by viewBinding()
    private val viewModel: SettingsViewModel by viewModel()

    private var curImageUri: Uri? = null
    private lateinit var uid: String

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uid = FirebaseUtil.auth.uid!!

        setupUi()
        subscribeToObservers()
        setupClickListeners()
        fetchCurrentUserInfo(uid)
    }

    private fun setupUi() {
        binding.etUsername.addTextChangedListener { binding.btnUpdateProfile.isEnabled = true }
        binding.etLocation.addTextChangedListener { binding.btnUpdateProfile.isEnabled = true }
        binding.etBio.addTextChangedListener { binding.btnUpdateProfile.isEnabled = true }
    }

    private fun fetchCurrentUserInfo(uid: String) = viewModel.getUser(uid)

    private fun setupClickListeners() {
        binding.btnUpdateProfile.setOnClickListener(this)
        binding.ivProfileImage.setOnClickListener(this)
    }

    private fun subscribeToObservers() {

        lifecycleScope.launchWhenResumed {
            viewModel.curImageUri.collect { uri ->
                curImageUri = uri

                binding.ivProfileImage.load(uri) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.updateProfileStatus.collect { resource: Resource<Any> ->
                when (resource) {
                    is Resource.Init -> Timber.d("Init profile update flow")
                    is Resource.Loading -> {
                        binding.settingsProgressBar.isVisible = true
                        binding.btnUpdateProfile.isEnabled = false
                    }
                    is Resource.Success -> {
                        binding.settingsProgressBar.isVisible = false
                        binding.btnUpdateProfile.isEnabled = true

                        snackBar(getString(R.string.profile_updated))
                    }
                    is Resource.Failure -> {
                        binding.settingsProgressBar.isVisible = false
                        binding.btnUpdateProfile.isEnabled = true

                        Timber.e(resource.message ?: getString(R.string.unknown_error))
                        snackBar(resource.message ?: getString(R.string.unknown_error))
                    }
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.getUserStatus.collect { resource: Resource<User> ->
                when (resource) {
                    is Resource.Loading -> binding.settingsProgressBar.isVisible = true
                    is Resource.Success -> {
                        binding.settingsProgressBar.isVisible = false

                        val data = resource.data as User
                        updateUiProfileInfo(data)
                    }
                    is Resource.Failure -> {
                        binding.settingsProgressBar.isVisible = false

                        Timber.e(resource.message ?: getString(R.string.unknown_error))
                        snackBar(resource.message ?: getString(R.string.unknown_error))
                    }
                }
            }
        }
    }

    private fun updateUiProfileInfo(data: User) {
        binding.ivProfileImage.load(data.photoUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        binding.etUsername.setText(data.username)
        binding.etBio.setText(data.bio)
        binding.etLocation.setText(data.location)
        binding.btnUpdateProfile.isEnabled = false
    }

    private fun updateUserProfileInfo() {
        val username = binding.etUsername.text?.trim().toString()
        val location = binding.etLocation.text?.trim().toString()
        val bio = binding.etBio.text?.trim().toString()

        val body = ProfileUpdateRequestBody(uid, username, bio, location, curImageUri)

        viewModel.updateProfile(body)
    }

    private fun launchImagePicker() {
        ImagePicker.with(this)
                .crop()
                .compress(2048)
                .start()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnUpdateProfile -> updateUserProfileInfo()
            R.id.ivProfileImage -> launchImagePicker()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri: Uri? = data?.data

                fileUri?.let {
                    viewModel.setCurrentImageUri(it)
                    binding.btnUpdateProfile.isEnabled = true
                }
            }
            ImagePicker.RESULT_ERROR -> snackBar(ImagePicker.getError(data))
            else -> snackBar("Task cancelled!")
        }
    }
}
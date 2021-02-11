/*
 * Created by Karic Kenan on 3.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.createpost.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.pandora.bottomnavigator.BottomNavigator
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.aethibo.fireshare.R
import io.aethibo.fireshare.databinding.FragmentCreatePostBinding
import io.aethibo.fireshare.domain.request.PostRequestBody
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.createpost.viewmodel.CreatePostViewModel
import io.aethibo.fireshare.ui.utils.slideUpViews
import io.aethibo.fireshare.ui.utils.snackBar
import kotlinx.android.synthetic.main.fragment_create_post.*
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CreatePostFragment : Fragment(R.layout.fragment_create_post), View.OnClickListener {

    private val binding: FragmentCreatePostBinding by viewBinding()
    private val viewModel: CreatePostViewModel by viewModel()

    private lateinit var navigator: BottomNavigator

    companion object {
        fun newInstance() = CreatePostFragment()
    }

    private lateinit var cropContent: ActivityResultLauncher<Any?>

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {

        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(requireContext())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cropContent = registerForActivityResult(cropActivityResultContract) {
            it?.let {
                viewModel.setCurrentImageUri(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigator = BottomNavigator.provide(requireActivity())

        setupClickListeners()
        subscribeToObservers()
        animateUi()
    }

    private fun animateUi() =
        slideUpViews(
            requireContext(),
            binding.ivPostImage,
            binding.btnSetPostImage,
            binding.tilPostText,
            binding.btnPost
        )

    private fun subscribeToObservers() {

        lifecycleScope.launchWhenResumed {
            viewModel.createPostStatus.collect { resource: Resource<Any> ->
                when (resource) {
                    is Resource.Init -> Timber.i("Creating post initialized")
                    is Resource.Loading -> {
                        Timber.i("Loading")
                        binding.createPostProgressBar.isVisible = true
                    }
                    is Resource.Success -> {
                        Timber.i("Success")
                        binding.createPostProgressBar.isVisible = false
                        navigator.clearAll()
                    }
                    is Resource.Failure -> {
                        Timber.e(resource.message.toString())
                        binding.createPostProgressBar.isVisible = false

                        snackBar(resource.message.toString())
                    }
                }
            }
        }

        viewModel.currentImageUrl.observe(viewLifecycleOwner) { value: Uri ->
            currentImageUri = value

            btnSetPostImage.isVisible = false
            binding.ivPostImage.load(currentImageUri) {
                crossfade(true)
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSetPostImage.setOnClickListener(this)
        binding.ivPostImage.setOnClickListener(this)
        binding.btnPost.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSetPostImage, R.id.ivPostImage -> cropContent.launch(null)
            R.id.btnPost -> currentImageUri?.let { uri ->
                val body = PostRequestBody(
                    caption = binding.etPostDescription.text.toString(),
                    imageUri = uri
                )
                viewModel.createPost(body)
            } ?: snackBar(getString(R.string.error_no_image_chosen))
        }
    }
}
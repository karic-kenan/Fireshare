/*
 * Created by Karic Kenan on 3.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.createpost.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.github.dhaval2404.imagepicker.ImagePicker
import com.pandora.bottomnavigator.BottomNavigator
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

    private var currentImageUri: Uri? = null

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

    private fun launchImagePicker() {
        ImagePicker.with(this)
            .crop()
            .compress(2048)
            .start()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSetPostImage, R.id.ivPostImage -> launchImagePicker()
            R.id.btnPost -> currentImageUri?.let { uri ->
                val body = PostRequestBody(
                    caption = binding.etPostDescription.text.toString(),
                    imageUri = uri
                )
                viewModel.createPost(body)
            } ?: snackBar(getString(R.string.error_no_image_chosen))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri: Uri? = data?.data

                fileUri?.let {
                    binding.ivPostImage.load(it)
                    viewModel.setCurrentImageUri(it)
                }
            }
            ImagePicker.RESULT_ERROR -> snackBar(ImagePicker.getError(data))
            else -> snackBar("Task cancelled!")
        }
    }
}
package io.aethibo.fireshare.features.add.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.utils.EventObserver
import io.aethibo.fireshare.databinding.FragmentAddPostBinding
import io.aethibo.fireshare.features.add.viewmodel.AddPostViewModel
import io.aethibo.fireshare.features.utils.slideUpViews
import io.aethibo.fireshare.features.utils.snackBar
import org.koin.android.viewmodel.ext.android.viewModel

class AddPostFragment : Fragment(R.layout.fragment_add_post), View.OnClickListener {

    private val viewModel: AddPostViewModel by viewModel()
    private val binding: FragmentAddPostBinding by viewBinding()

    private lateinit var cropContent: ActivityResultLauncher<Any?>

    // TODO: introduce image compressor
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

        subscribeToObservers()
        setupButtonClickListeners()

        slideUpViews(requireContext(),
                binding.ivPostImage,
                binding.btnSetPostImage,
                binding.tilPostText,
                binding.btnPost
        )
    }

    private fun subscribeToObservers() {

        viewModel.createPostStatus.observe(viewLifecycleOwner, EventObserver(
                onLoading = {
                    binding.createPostProgressBar.isVisible = true
                },
                onSuccess = {
                    binding.createPostProgressBar.isVisible = false
                    // TODO: navigate back to timeline
                },
                onError = {
                    binding.createPostProgressBar.isVisible = false
                    snackBar(it)
                }
        ))

        viewModel.currentImageUrl.observe(viewLifecycleOwner) {
            currentImageUri = it
            binding.btnSetPostImage.isVisible = false
            binding.ivPostImage.load(currentImageUri) {
                crossfade(true)
            }
        }
    }

    private fun setupButtonClickListeners() {
        binding.btnSetPostImage.setOnClickListener(this)
        binding.ivPostImage.setOnClickListener(this)
        binding.btnPost.setOnClickListener(this)
    }

    private fun publishPost() {
        currentImageUri?.let { uri ->
            viewModel.createPost(uri, binding.etPostDescription.text?.trim().toString())
        } ?: snackBar(getString(R.string.error_no_image_chosen))
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSetPostImage -> cropContent.launch(null)
            R.id.ivPostImage -> cropContent.launch(null)
            R.id.btnPost -> publishPost()
        }
    }
}
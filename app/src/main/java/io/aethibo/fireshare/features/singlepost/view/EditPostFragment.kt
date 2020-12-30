package io.aethibo.fireshare.features.singlepost.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.entities.PostToUpdate
import io.aethibo.fireshare.core.utils.EventObserver
import io.aethibo.fireshare.databinding.FragmentEditPostBinding
import io.aethibo.fireshare.features.singlepost.viewmodel.DetailPostViewModel
import io.aethibo.fireshare.features.utils.BasePostFragment
import io.aethibo.fireshare.features.utils.slideUpViews
import io.aethibo.fireshare.features.utils.snackBar
import org.koin.android.viewmodel.ext.android.viewModel

class EditPostFragment : BasePostFragment(R.layout.fragment_edit_post), View.OnClickListener {

    private val args: EditPostFragmentArgs by navArgs()
    private val binding: FragmentEditPostBinding by viewBinding()
    private val viewModel: DetailPostViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupClickListeners()
        subscribeToObservers()

        slideUpViews(requireContext(), binding.ivEditPost, binding.tilEditPost, binding.mbEditPostSave)
    }

    private fun subscribeToObservers() {
        viewModel.updatePostStatus.observe(viewLifecycleOwner, EventObserver(
                onLoading = {
                    binding.pbEditPost.isVisible = true
                },
                onSuccess = {
                    binding.pbEditPost.isVisible = false
                    findNavController().popBackStack(R.id.editPostFragment, true)
                },
                onError = {
                    binding.pbEditPost.isVisible = false
                    snackBar(it)
                }
        ))
    }

    private fun setupClickListeners() {
        binding.mbEditPostSave.setOnClickListener(this)
    }

    private fun setupView() {
        binding.ivEditPost.load(args.post.imageUrl) {
            crossfade(true)
        }

        binding.etEditPost.setText(args.post.caption)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.mbEditPostSave -> {
                viewModel.updatePost(PostToUpdate(args.post.id, binding.etEditPost.text?.trim()?.toString()))
            }
        }
    }
}
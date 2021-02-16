/*
 * Created by Karic Kenan on 15.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.comments.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.R
import io.aethibo.fireshare.databinding.FragmentCommentsBinding
import io.aethibo.fireshare.domain.Comment
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.comments.adapter.CommentsAdapter
import io.aethibo.fireshare.ui.comments.viewmodel.CommentsViewModel
import io.aethibo.fireshare.ui.utils.snackBar
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class CommentsFragment : Fragment(R.layout.fragment_comments), View.OnClickListener {

    private val binding: FragmentCommentsBinding by viewBinding()
    private val viewModel: CommentsViewModel by viewModel()
    private val commentsAdapter: CommentsAdapter by lazy { CommentsAdapter() }
    private lateinit var postId: String

    companion object {
        fun newInstance(postId: String) = CommentsFragment().apply {
            arguments = Bundle().apply {
                putString("postId", postId)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getString("postId")?.let { postId ->
            this.postId = postId
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCommentsForPost(postId)

        setOnClickListeners()
        setupCommentRefreshListener()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.commentsForPosts.collect { value: Resource<List<Comment>> ->
                when (value) {
                    is Resource.Init -> Timber.d("Fetching comments for post ID: $postId")
                    is Resource.Loading -> binding.commentsProgressBar.isVisible = true
                    is Resource.Success -> {
                        val data: List<Comment> = value.data as List<Comment>
                        setupAdapter(data)

                        binding.commentsProgressBar.isVisible = false
                    }
                    is Resource.Failure -> {
                        binding.commentsProgressBar.isVisible = false

                        snackBar(value.message ?: "Unknown error occurred!")
                    }
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.createCommentStatus.collect { value: Resource<Comment> ->
                when (value) {
                    is Resource.Init -> Timber.d("Creating comment for post ID: $postId")
                    is Resource.Loading -> {
                        binding.commentsProgressBar.isVisible = true
                        binding.ibAddComment.isEnabled = false
                    }
                    is Resource.Success -> {
                        viewModel.getCommentsForPost(postId)

                        binding.commentsProgressBar.isVisible = false
                        binding.ibAddComment.isEnabled = true
                    }
                    is Resource.Failure -> {
                        binding.commentsProgressBar.isVisible = false
                        binding.ibAddComment.isEnabled = true

                        snackBar(value.message ?: "Unknown error occurred!")
                    }
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.deleteCommentStatus.collect { value: Resource<Comment> ->
                when (value) {
                    is Resource.Init -> Timber.d("Deleting comment for post ID: $postId")
                    is Resource.Loading -> binding.commentsProgressBar.isVisible = true
                    is Resource.Success -> {
                        viewModel.getCommentsForPost(postId)

                        binding.commentsProgressBar.isVisible = false
                    }
                    is Resource.Failure -> {
                        binding.commentsProgressBar.isVisible = false

                        snackBar(value.message ?: "Unknown error occurred!")
                    }
                }
            }
        }
    }

    private fun setupCommentRefreshListener() =
            binding.commentsRefreshLayout.setOnRefreshListener {
                viewModel.getCommentsForPost(postId).also {
                    binding.commentsRefreshLayout.isRefreshing = false
                }
            }

    private fun setOnClickListeners() {
        binding.ibAddComment.setOnClickListener(this)

        commentsAdapter.setOnUserClickListener { comment ->
            /* if (FirebaseAuth.getInstance().uid == comment.userId) {
                requireActivity().navigation.selectedItemId = R.id.profileFragment
                return@setOnUserClickListener
            }*/

            println("User clicked: ${comment.authorUsername}")

            // TODO: Navigate to other user profile
        }

        commentsAdapter.setOnCommentMenuClickListener { comment -> viewModel.showCommentContextMenu(requireContext(), layoutInflater, BottomNavigator.provide(requireActivity()), comment) }
    }

    private fun setupAdapter(data: List<Comment>) {
        commentsAdapter.submitList(data)
        commentsAdapter.hasStableIds()

        binding.rvCommentsList.apply {
            adapter = commentsAdapter
            itemAnimator = null
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ibAddComment -> viewModel.createComment(postId, binding.etComment.text?.trim()?.toString()).also {
                binding.etComment.text?.clear()
            }
        }
    }
}
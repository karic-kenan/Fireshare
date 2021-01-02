package io.aethibo.fireshare.features.comment.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.utils.EventObserver
import io.aethibo.fireshare.core.utils.FirebaseUtil.auth
import io.aethibo.fireshare.databinding.FragmentCommentsBinding
import io.aethibo.fireshare.features.comment.adapter.CommentsAdapter
import io.aethibo.fireshare.features.comment.viewmodel.CommentsViewModel
import io.aethibo.fireshare.features.utils.snackBar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class CommentsFragment : Fragment(R.layout.fragment_comments), View.OnClickListener {

    private val binding: FragmentCommentsBinding by viewBinding()
    private val viewModel: CommentsViewModel by viewModel()
    private val commentsAdapter: CommentsAdapter by lazy { CommentsAdapter() }
    private val args: CommentsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCommentsForPost(args.postId)

        setupAdapter()
        setOnClickListeners()
        setupCommentRefreshListener()
        subscribeToObservers()
    }

    private fun setupCommentRefreshListener() =
            binding.commentsRefreshLayout.setOnRefreshListener {
                viewModel.getCommentsForPost(args.postId).also {
                    binding.commentsRefreshLayout.isRefreshing = false
                }
            }

    private fun setupAdapter() = binding.rvCommentsList.apply {
        adapter = commentsAdapter
        itemAnimator = null
    }

    private fun setOnClickListeners() {
        binding.ibAddComment.setOnClickListener(this)

        commentsAdapter.setOnUserClickListener { comment ->
            if (auth.uid == comment.userId) {
                requireActivity().navigation.selectedItemId = R.id.profileFragment
                return@setOnUserClickListener
            }

            // TODO: Navigate to other user profile
        }

        commentsAdapter.setOnMenuCommentClickListener { comment ->
            viewModel.showCommentMenu(requireContext(), layoutInflater, findNavController(), comment)
        }
    }

    private fun subscribeToObservers() {

        viewModel.commentsForPosts.observe(viewLifecycleOwner, EventObserver(
                onLoading = {
                    binding.commentsProgressBar.isVisible = true
                },
                onSuccess = { comments ->
                    commentsAdapter.comments = comments

                    binding.commentsProgressBar.isVisible = false
                },
                onError = {
                    binding.commentsProgressBar.isVisible = false
                    snackBar(it)
                }
        ))

        viewModel.createCommentStatus.observe(viewLifecycleOwner, EventObserver(
                onLoading = {
                    binding.commentsProgressBar.isVisible = true
                    binding.ibAddComment.isEnabled = false
                },
                onSuccess = { comment ->
                    commentsAdapter.comments += comment

                    binding.commentsProgressBar.isVisible = false
                    binding.ibAddComment.isEnabled = true
                },
                onError = {
                    binding.commentsProgressBar.isVisible = false
                    binding.ibAddComment.isEnabled = true

                    snackBar(it)
                }
        ))

        viewModel.deleteCommentStatus.observe(viewLifecycleOwner, EventObserver(
                onLoading = {
                    binding.commentsProgressBar.isVisible = true
                },
                onSuccess = { comment ->
                    commentsAdapter.comments -= comment

                    binding.commentsProgressBar.isVisible = false
                },
                onError = {
                    binding.commentsProgressBar.isVisible = false
                    snackBar(it)
                }
        ))
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ibAddComment -> viewModel.createComment(
                    postId = args.postId,
                    commentText = binding.etComment.text?.trim()?.toString())
                    .also { binding.etComment.text?.clear() }
        }
    }
}
package io.aethibo.fireshare.features.singlepost.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.utils.EventObserver
import io.aethibo.fireshare.databinding.FragmentCommentsBinding
import io.aethibo.fireshare.features.singlepost.misc.CommentsAdapter
import io.aethibo.fireshare.features.singlepost.viewmodel.CommentsViewModel
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
        subscribeToObservers()
    }

    private fun setupAdapter() = binding.rvCommentsList.apply {
        adapter = commentsAdapter
        itemAnimator = null
    }

    private fun setOnClickListeners() {
        binding.ibAddComment.setOnClickListener(this)

        commentsAdapter.setOnUserClickListener { comment ->
            if (FirebaseAuth.getInstance().uid == comment.userId) {
                requireActivity().navigation.selectedItemId = R.id.profileFragment
                return@setOnUserClickListener
            }

            println("User clicked: ${comment.authorUsername}")

            // TODO: Navigate to other user profile
        }

        commentsAdapter.setOnDeleteCommentClickListener { comment ->
            // TODO: Show pop-up menu to editing/deleting the comment

            viewModel.deleteComment(comment)
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
            R.id.ibAddComment -> viewModel.createComment(args.postId, binding.etComment.text?.trim()?.toString()).also { binding.etComment.text?.clear() }
        }
    }
}
/*
 * Created by Karic Kenan on 9.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.postdetail.view

import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.R
import io.aethibo.fireshare.databinding.LayoutItemPostBinding
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.framework.utils.FirebaseUtil.auth
import io.aethibo.fireshare.ui.postdetail.viewmodel.DetailPostViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class DetailPostFragment : Fragment(R.layout.layout_item_post) {

    companion object {
        fun newInstance(post: Post) = DetailPostFragment().apply {
            arguments = Bundle().apply {
                putParcelable("post", post)
            }
        }
    }

    private val binding: LayoutItemPostBinding by viewBinding()
    private val viewModel: DetailPostViewModel by viewModel()

    private lateinit var post: Post
    private lateinit var navigator: BottomNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator = BottomNavigator.provide(requireActivity())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getParcelable<Post>("post")?.let { post ->
            this.post = post
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.postMenu.isVisible = auth.uid == post.ownerId

        setupViews()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {

    }

    private fun setupViews() {
        binding.postAvatar.load(post.authorProfilePictureUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        binding.postImage.load(post.imageUrl) {
            crossfade(true)
            transformations(RoundedCornersTransformation(10f))
        }
        binding.postUsername.text = post.authorUsername
        binding.postDate.text = DateUtils.getRelativeTimeSpanString(post.timestamp)
        binding.postDescription.text = post.caption

        val likeCount = post.likedBy.size
        binding.postLikeCountTxt.text = when {
            likeCount <= 0 -> getString(R.string.single_post_no_likes)
            likeCount == 1 -> getString(R.string.single_post_one_like)
            else -> getString(R.string.single_post_multiple_likes, likeCount)
        }

        val commentCount = post.likedBy.size
        binding.postCommentCountTxt.text = when {
            likeCount <= 0 -> getString(R.string.single_post_no_comments)
            likeCount == 1 -> getString(R.string.single_post_one_comment)
            else -> getString(R.string.single_post_multiple_comments, commentCount)
        }

        binding.postMenu.setOnClickListener {
            viewModel.singlePostOptionsMenuClicked(
                requireContext(),
                layoutInflater,
                post,
                navigator
            )
        }

        binding.postCommentButton.setOnClickListener {
            // navigator.addFragment(CommentsFragment.newInstance(post.id))
        }

        binding.postDivider.isVisible = false
    }

    override fun onDestroy() {
        println("AAA detail post fragment destroyed")
        super.onDestroy()
    }
}
/*
 * Created by Karic Kenan on 9.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.domain.PostToCommentModel
import io.aethibo.fireshare.framework.utils.FirebaseUtil
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.comments.view.CommentsFragment
import io.aethibo.fireshare.ui.profile.adapter.ProfilePostAdapter
import io.aethibo.fireshare.ui.profile.viewmodel.ProfileViewModel
import io.aethibo.fireshare.ui.utils.snackBar
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

abstract class BaseProfilePostFragment(layoutId: Int) : Fragment(layoutId) {

    protected val profilePostAdapter: ProfilePostAdapter by lazy { ProfilePostAdapter() }
    protected lateinit var navigator: BottomNavigator
    private val viewModel: ProfileViewModel by viewModel()

    protected abstract val baseViewModel: BasePostViewModel

    private var currentLikedIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigator = BottomNavigator.provide(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()
        setupAdapterClickListeners()
    }

    private fun setupAdapterClickListeners() {

        profilePostAdapter.setOnLikeClickListener { post, position ->
            currentLikedIndex = position
            post.isLiked = !post.isLiked
            baseViewModel.toggleLikeForPost(post)
        }

        profilePostAdapter.setOnMenuClickListener { post, _ ->
            viewModel.singlePostOptionsMenuClicked(requireContext(), layoutInflater, post)
        }

        profilePostAdapter.setOnCommentClickListener { postId, imageUrl, ownerId, _ ->
            val tempPost = PostToCommentModel(postId, imageUrl, ownerId)
            navigator.addFragment(CommentsFragment.newInstance(tempPost))
        }
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenResumed {
            baseViewModel.likePostStatus.collect { value: Resource<Boolean> ->
                when (value) {
                    is Resource.Init -> Timber.d("Initialized post liking")
                    is Resource.Loading -> currentLikedIndex?.let { index ->
                        profilePostAdapter.peek(index)?.isLiking = true
                        profilePostAdapter.notifyItemChanged(index)
                    }
                    is Resource.Success -> currentLikedIndex?.let { index ->
                        val uid = FirebaseUtil.auth.uid!!
                        val isLiked = value.data as Boolean

                        profilePostAdapter.peek(index)?.apply {
                            this.isLiked = isLiked
                            isLiking = false

                            viewModel.handleLikeFeed(isLiked, this.id, this.ownerId, this.imageUrl)

                            if (isLiked) likedBy += uid else likedBy -= uid
                        }

                        profilePostAdapter.notifyItemChanged(index)
                    }
                    is Resource.Failure -> currentLikedIndex?.let { index ->
                        profilePostAdapter.peek(index)?.isLiking = false
                        profilePostAdapter.notifyItemChanged(index)

                        snackBar(value.message ?: "Unknown error occurred!")
                    }
                }
            }
        }
    }
}
/*
 * Created by Karic Kenan on 17.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.timeline.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.R
import io.aethibo.fireshare.databinding.FragmentTimelineBinding
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.framework.utils.FirebaseUtil
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.comments.view.CommentsFragment
import io.aethibo.fireshare.ui.othersprofile.view.OthersProfileFragment
import io.aethibo.fireshare.ui.timeline.adapter.TimelineAdapter
import io.aethibo.fireshare.ui.timeline.viewmodel.TimelineViewModel
import io.aethibo.fireshare.ui.utils.snackBar
import kotlinx.android.synthetic.main.fragment_timeline.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class TimelineFragment : Fragment(R.layout.fragment_timeline) {

    private val viewModel: TimelineViewModel by viewModel()
    private val binding: FragmentTimelineBinding by viewBinding()
    private val timelineAdapter: TimelineAdapter by lazy { TimelineAdapter() }

    private lateinit var navigator: BottomNavigator
    private var currentLikedIndex: Int? = null

    companion object {
        fun newInstance() = TimelineFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        navigator = BottomNavigator.provide(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTimeline()

        setupAdapter()
        setupAdapterClickListeners()
        subscribeToObservers()
    }

    private fun setupAdapter() {
        rvTimeline.adapter = timelineAdapter
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.timelineStatus.collectLatest { value: Resource<List<Post>> ->
                when (value) {
                    is Resource.Loading -> binding.pbTimeline.isVisible = true
                    is Resource.Success -> {
                        binding.pbTimeline.isVisible = false

                        val result = value.data as List<Post>
                        timelineAdapter.submitList(result)
                    }
                    is Resource.Failure -> {
                        binding.pbTimeline.isVisible = false
                        Timber.e("Error: ${value.message ?: "Unknown error occurred!"}")
                        snackBar("Error: ${value.message ?: "Unknown error occurred!"}")
                    }
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.likePostStatus.collect { value: Resource<Boolean> ->
                when (value) {
                    is Resource.Init -> Timber.d("Initialized post liking")
                    is Resource.Loading -> currentLikedIndex?.let { index ->
//                        profilePostAdapter.peek(index)?.isLiking = true
//                        profilePostAdapter.notifyItemChanged(index)
                    }
                    is Resource.Success -> currentLikedIndex?.let { index ->
                        val uid = FirebaseUtil.auth.uid!!
                        val isLiked = value.data as Boolean

                        /*profilePostAdapter.peek(index)?.apply {
                            this.isLiked = isLiked
                            isLiking = false

                            if (isLiked) likedBy += uid else likedBy -= uid
                        }

                        profilePostAdapter.notifyItemChanged(index)*/
                    }
                    is Resource.Failure -> currentLikedIndex?.let { index ->
//                        profilePostAdapter.peek(index)?.isLiking = false
//                        profilePostAdapter.notifyItemChanged(index)

                        snackBar(value.message ?: "Unknown error occurred!")
                    }
                }
            }
        }
    }

    private fun setupAdapterClickListeners() {
        timelineAdapter.apply {

            setOnLikeClickListener { post, position ->
                currentLikedIndex = position
                post.isLiked = !post.isLiked
                viewModel.toggleLikeForPost(post)
            }

            setOnUserClickListener { userId ->
                navigator.addFragment(OthersProfileFragment.newInstance(userId))
            }

            setOnCommentClickListener { postId ->
                navigator.addFragment(CommentsFragment.newInstance(postId))
            }

            setOnMenuClickListener { post, position ->

            }
        }
    }
}
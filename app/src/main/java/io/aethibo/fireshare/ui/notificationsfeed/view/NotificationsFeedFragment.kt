/*
 * Created by Karic Kenan on 23.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.notificationsfeed.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import io.aethibo.fireshare.R
import io.aethibo.fireshare.databinding.FragmentNotificationsFeedBinding
import io.aethibo.fireshare.domain.ActivityFeedItem
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.notificationsfeed.adapter.FeedAdapter
import io.aethibo.fireshare.ui.notificationsfeed.viewmodel.FeedViewModel
import io.aethibo.fireshare.ui.utils.snackBar
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class NotificationsFeedFragment : Fragment(R.layout.fragment_notifications_feed) {

    private val viewModel: FeedViewModel by viewModel()
    private val binding: FragmentNotificationsFeedBinding by viewBinding()
    private val feedAdapter: FeedAdapter by lazy { FeedAdapter() }

    companion object {
        fun newInstance() = NotificationsFeedFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFeedList()

        setupAdapter()
        subscribeToObservers()
    }

    private fun setupAdapter() {
        binding.rvFeedList.adapter = feedAdapter
    }

    private fun subscribeToObservers() {

        lifecycleScope.launchWhenResumed {
            viewModel.feedListStatus.collectLatest { value: Resource<List<ActivityFeedItem>> ->
                when (value) {
                    is Resource.Loading -> binding.feedProgressBar.isVisible = true
                    is Resource.Success -> {
                        binding.feedProgressBar.isVisible = false

                        val data = value.data as List<ActivityFeedItem>
                        feedAdapter.submitList(data)
                    }
                    is Resource.Failure -> {
                        binding.feedProgressBar.isVisible = false

                        Timber.e("Error: ${value.message ?: "Unknown error occurred"}")
                        snackBar("Error: ${value.message ?: "Unknown error occurred"}")
                    }
                }
            }
        }
    }
}
/*
 * Created by Karic Kenan on 17.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.timeline.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.ui.timeline.adapter.TimelineAdapter
import io.aethibo.fireshare.ui.timeline.viewmodel.TimelineViewModel
import kotlinx.android.synthetic.main.fragment_timeline.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class TimelineFragment : Fragment(R.layout.fragment_timeline) {

    private val viewModel: TimelineViewModel by viewModel()
    private val timelineAdapter: TimelineAdapter by lazy { TimelineAdapter() }

    companion object {
        fun newInstance() = TimelineFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTimeline()

        setupAdapter()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenResumed {
            viewModel.timelineStatus.collectLatest { value: Resource<List<Post>> ->
                when (value) {
                    is Resource.Loading -> Timber.d("Loading get timeline")
                    is Resource.Success -> {
                        val result = value.data as List<Post>
                        Timber.d("Results IDS: $result")
                        timelineAdapter.submitList(result)
                    }
                    is Resource.Failure -> Timber.e("Error in timeline")
                }
            }
        }
    }

    private fun setupAdapter() {
        rvTimeline.adapter = timelineAdapter
    }
}
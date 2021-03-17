/*
 * Created by Karic Kenan on 17.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.timeline.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.usecases.GetTimelineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimelineViewModel(private val getTimeline: GetTimelineUseCase, private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : ViewModel() {

    private val _timelineStatus: MutableStateFlow<Resource<List<Post>>> =
        MutableStateFlow(Resource.Loading())
    val timelineStatus: StateFlow<Resource<List<Post>>>
        get() = _timelineStatus

    fun getTimeline() {
        viewModelScope.launch(dispatcher) {
            val result: Resource<List<Post>> = getTimeline.invoke()

            _timelineStatus.value = result
        }
    }
}
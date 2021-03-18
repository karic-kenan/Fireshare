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
import io.aethibo.fireshare.usecases.LikePostUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimelineViewModel(private val getTimeline: GetTimelineUseCase, private val likePost: LikePostUseCase, private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : ViewModel() {

    private val _timelineStatus: MutableStateFlow<Resource<List<Post>>> =
            MutableStateFlow(Resource.Loading())
    val timelineStatus: StateFlow<Resource<List<Post>>>
        get() = _timelineStatus

    private val _likePostStatus: MutableStateFlow<Resource<Boolean>> = MutableStateFlow(Resource.Init())
    val likePostStatus: StateFlow<Resource<Boolean>>
        get() = _likePostStatus

    fun getTimeline() {
        viewModelScope.launch(dispatcher) {
            val result: Resource<List<Post>> = getTimeline.invoke()

            _timelineStatus.value = result
        }
    }

    fun toggleLikeForPost(post: Post) {
        _likePostStatus.value = Resource.Loading()

        viewModelScope.launch(dispatcher) {
            val result: Resource<Boolean> = likePost.invoke(post)

            _likePostStatus.value = result
        }
    }
}
/*
 * Created by Karic Kenan on 23.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.notificationsfeed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.aethibo.fireshare.domain.ActivityFeedItem
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.usecases.GetNotificationFeedUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FeedViewModel(private val feedList: GetNotificationFeedUseCase, private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : ViewModel() {

    private val _feedListStatus: MutableStateFlow<Resource<List<ActivityFeedItem>>> = MutableStateFlow(Resource.Loading())
    val feedListStatus: StateFlow<Resource<List<ActivityFeedItem>>>
        get() = _feedListStatus

    fun getFeedList() {
        viewModelScope.launch(dispatcher) {
            val result: Resource<List<ActivityFeedItem>> = feedList.invoke()

            _feedListStatus.value = result
        }
    }
}
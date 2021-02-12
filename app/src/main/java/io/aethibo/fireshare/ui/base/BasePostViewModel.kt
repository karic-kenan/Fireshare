/*
 * Created by Karic Kenan on 3.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.usecases.GetSingleUserUseCase
import io.aethibo.fireshare.usecases.LikePostUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BasePostViewModel(private val getSingleUser: GetSingleUserUseCase, private val likePost: LikePostUseCase, private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : ViewModel() {

    private val _profileMeta: MutableStateFlow<Resource<User>> = MutableStateFlow(Resource.Init())
    val profileMeta: StateFlow<Resource<User>>
        get() = _profileMeta

    private val _likePostStatus: MutableStateFlow<Resource<Boolean>> = MutableStateFlow(Resource.Init())
    val likePostStatus: StateFlow<Resource<Boolean>>
        get() = _likePostStatus

    fun loadProfile(uid: String) {
        _profileMeta.value = Resource.Loading()

        viewModelScope.launch(dispatcher) {
            val result: Resource<User> = getSingleUser.invoke(uid)

            _profileMeta.value = result
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
/*
 * Created by Karic Kenan on 3.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.createpost.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.aethibo.fireshare.FireshareApp
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.request.PostRequestBody
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.usecases.CreatePostUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreatePostViewModel(
    private val createPost: CreatePostUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _createPostStatus: MutableStateFlow<Resource<Any>> =
        MutableStateFlow(Resource.Init())
    val createPostStatus: StateFlow<Resource<Any>>
        get() = _createPostStatus

    private val _currentImageUri: MutableLiveData<Uri> = MutableLiveData()
    val currentImageUrl: LiveData<Uri>
        get() = _currentImageUri

    fun setCurrentImageUri(uri: Uri) {
        _currentImageUri.postValue(uri)
    }

    fun createPost(body: PostRequestBody) {
        when {
            body.caption.isEmpty() -> {
                val error = FireshareApp.instance.getString(R.string.error_input_empty)
                _createPostStatus.value = Resource.Failure(error)
            }
            else -> {
                _createPostStatus.value = Resource.Loading()

                viewModelScope.launch(dispatcher) {
                    val result: Resource<Any> = createPost.invoke(body)
                    _createPostStatus.value = result
                }
            }
        }
    }
}
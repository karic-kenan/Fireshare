/*
 * Created by Karic Kenan on 8.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.settings.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.aethibo.fireshare.FireshareApp
import io.aethibo.fireshare.R
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.domain.request.ProfileUpdateRequestBody
import io.aethibo.fireshare.framework.utils.AppConst
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.usecases.GetSingleUserUseCase
import io.aethibo.fireshare.usecases.UpdateUserProfileUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
        private val updateUserInfo: UpdateUserProfileUseCase,
        private val getSingleUser: GetSingleUserUseCase,
        private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _updateProfileStatus: MutableStateFlow<Resource<Any>> =
            MutableStateFlow(Resource.Init())
    val updateProfileStatus: StateFlow<Resource<Any>>
        get() = _updateProfileStatus

    private val _getUserStatus: MutableStateFlow<Resource<User>> =
            MutableStateFlow(Resource.Loading())
    val getUserStatus: StateFlow<Resource<User>>
        get() = _getUserStatus

    private val _curImageUri: MutableStateFlow<Uri> = MutableStateFlow(Uri.EMPTY)
    val curImageUri: StateFlow<Uri>
        get() = _curImageUri

    fun updateProfile(body: ProfileUpdateRequestBody) {
        when {
            body.username.isEmpty() or body.location.isEmpty() or body.bio.isEmpty() -> {
                val error = FireshareApp.instance.getString(R.string.error_input_empty)
                _updateProfileStatus.value = Resource.Failure(error)
            }
            body.username.length < AppConst.MIN_USERNAME_LENGTH -> {
                val error = FireshareApp.instance.getString(
                        R.string.error_username_too_short,
                        AppConst.MIN_USERNAME_LENGTH
                )
                _updateProfileStatus.value = Resource.Failure(error)
            }
            else -> {
                _updateProfileStatus.value = Resource.Loading()

                viewModelScope.launch(dispatcher) {
                    val result: Resource<Any> = updateUserInfo.invoke(body)
                    _updateProfileStatus.value = result
                }
            }
        }
    }

    fun getUser(uid: String) {
        viewModelScope.launch(dispatcher) {
            val result: Resource<User> = getSingleUser.invoke(uid)
            _getUserStatus.value = result
        }
    }

    fun setCurrentImageUri(uri: Uri) {
        _curImageUri.value = uri
    }
}
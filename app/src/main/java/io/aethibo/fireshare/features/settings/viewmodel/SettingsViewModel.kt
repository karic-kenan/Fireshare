package io.aethibo.fireshare.features.settings.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.aethibo.fireshare.R
import io.aethibo.fireshare.core.entities.ProfileUpdate
import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.AppConst.MIN_USERNAME_LENGTH
import io.aethibo.fireshare.core.utils.Event
import io.aethibo.fireshare.core.utils.Resource
import io.aethibo.fireshare.domain.profile.IProfileUseCase
import io.aethibo.fireshare.domain.users.IUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
        private val profileUseCase: IProfileUseCase,
        private val userUseCase: IUserUseCase,
        private val applicationContext: Context,
        private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _updateProfileStatus: MutableLiveData<Event<Resource<Any>>> = MutableLiveData()
    val updateProfileStatus: LiveData<Event<Resource<Any>>>
        get() = _updateProfileStatus

    private val _getUserStatus: MutableLiveData<Event<Resource<User>>> = MutableLiveData()
    val getUserStatus: LiveData<Event<Resource<User>>>
        get() = _getUserStatus

    private val _curImageUri: MutableLiveData<Uri> = MutableLiveData()
    val curImageUri: LiveData<Uri>
        get() = _curImageUri

    fun updateProfile(profileUpdate: ProfileUpdate) {
        when {
            profileUpdate.displayName.isEmpty() or profileUpdate.username.isEmpty() or profileUpdate.bio.isEmpty() -> {
                val error = applicationContext.getString(R.string.error_input_empty)
                _updateProfileStatus.postValue(Event(Resource.Error(error)))
            }
            profileUpdate.username.length < MIN_USERNAME_LENGTH -> {
                val error = applicationContext.getString(R.string.error_username_too_short)
                _updateProfileStatus.postValue(Event(Resource.Error(error)))
            }
            else -> {
                _updateProfileStatus.postValue(Event(Resource.Loading()))

                viewModelScope.launch(dispatcher) {
                    val result = profileUseCase.updateProfile(profileUpdate)
                    _updateProfileStatus.postValue(Event(result))
                }
            }
        }
    }

    fun getUser(uid: String) {
        _getUserStatus.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result = userUseCase.getSingleUser(uid)
            _getUserStatus.postValue(Event(result))
        }
    }

    fun setCurrentImageUri(uri: Uri) {
        _curImageUri.postValue(uri)
    }
}
package io.aethibo.fireshare.features.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.Event
import io.aethibo.fireshare.core.utils.Resource
import io.aethibo.fireshare.domain.users.IUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val useCase: IUserUseCase, private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : ViewModel() {

    private val _profileMeta: MutableLiveData<Event<Resource<User>>> = MutableLiveData()
    val profileMeta: LiveData<Event<Resource<User>>>
        get() = _profileMeta

    fun loadProfile(uid: String) {
        _profileMeta.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result = useCase.getSingleUser(uid)
            _profileMeta.postValue(Event(result))
        }
    }
}
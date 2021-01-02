package io.aethibo.fireshare.features.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.aethibo.fireshare.core.data.pagingsource.ProfilePostsPagingSource
import io.aethibo.fireshare.core.entities.Post
import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.AppConst.PAGE_SIZE
import io.aethibo.fireshare.core.utils.Event
import io.aethibo.fireshare.core.utils.FirebaseUtil.firestore
import io.aethibo.fireshare.core.utils.Resource
import io.aethibo.fireshare.domain.users.IUserUseCase
import io.aethibo.fireshare.features.utils.BasePostViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userUseCase: IUserUseCase, private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : BasePostViewModel() {

    private val _profileMeta: MutableLiveData<Event<Resource<User>>> = MutableLiveData()
    val profileMeta: LiveData<Event<Resource<User>>>
        get() = _profileMeta

    fun getPagingFlow(uid: String): Flow<PagingData<Post>> {
        val pagingSource = ProfilePostsPagingSource(firestore, uid)

        return Pager(PagingConfig(PAGE_SIZE)) {
            pagingSource
        }.flow.cachedIn(viewModelScope)
    }

    fun loadProfile(uid: String) {
        _profileMeta.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result = userUseCase.getSingleUser(uid)
            _profileMeta.postValue(Event(result))
        }
    }
}
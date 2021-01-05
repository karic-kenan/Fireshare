package io.aethibo.fireshare.features.discovery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.Event
import io.aethibo.fireshare.core.utils.Resource
import io.aethibo.fireshare.domain.search.ISearchUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscoveryViewModel(private val searchUseCase: ISearchUseCase, private val dispatcher: CoroutineDispatcher = Dispatchers.Main): ViewModel() {

    private val _searchResults: MutableLiveData<Event<Resource<List<User>>>> = MutableLiveData()
    val searchResults: LiveData<Event<Resource<List<User>>>>
        get() = _searchResults

    fun searchUser(query: String) {
        if (query.isEmpty()) return

        _searchResults.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result = searchUseCase.searchUser(query)
            _searchResults.postValue(Event(result))
        }
    }
}
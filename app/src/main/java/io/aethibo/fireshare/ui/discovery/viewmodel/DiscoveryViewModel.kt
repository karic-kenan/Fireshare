/*
 * Created by Karic Kenan on 6.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.ui.discovery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.framework.utils.Resource
import io.aethibo.fireshare.usecases.SearchUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DiscoveryViewModel(private val search: SearchUserUseCase, private val dispatcher: CoroutineDispatcher = Dispatchers.Main) : ViewModel() {

    private val _searchResults: MutableStateFlow<Resource<List<User>>> = MutableStateFlow(Resource.Init())
    val searchResults: StateFlow<Resource<List<User>>>
        get() = _searchResults

    fun searchUser(query: String) {
        if (query.isEmpty()) return

        _searchResults.value = Resource.Loading()

        viewModelScope.launch(dispatcher) {
            val result: Resource<List<User>> = search.invoke(query)

            _searchResults.value = result
        }
    }
}
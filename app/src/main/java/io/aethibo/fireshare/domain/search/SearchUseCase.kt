package io.aethibo.fireshare.domain.search

import io.aethibo.fireshare.core.data.repositories.main.MainRepository
import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.Resource

class SearchUseCase(private val repository: MainRepository): ISearchUseCase {
    override suspend fun searchUser(query: String): Resource<List<User>> =
            repository.searchUser(query)
}
package io.aethibo.fireshare.domain.search

import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.Resource

interface ISearchUseCase {
    suspend fun searchUser(query: String): Resource<List<User>>
}
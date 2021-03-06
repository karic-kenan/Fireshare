/*
 * Created by Karic Kenan on 6.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.framework.utils.Resource

interface SearchUserUseCase {
    suspend operator fun invoke(query: String): Resource<List<User>>
}

class SearchUserUseCaseImpl(private val repository: MainRepository) : SearchUserUseCase {

    override suspend fun invoke(query: String): Resource<List<User>> =
            repository.searchUsers(query)
}
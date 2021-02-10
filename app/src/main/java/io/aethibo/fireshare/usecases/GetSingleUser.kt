/*
 * Created by Karic Kenan on 8.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.framework.utils.Resource

interface GetSingleUserUseCase {
    suspend operator fun invoke(uid: String): Resource<User>
}

class GetSingleUserUseCaseImpl(private val repository: MainRepository) : GetSingleUserUseCase {
    override suspend fun invoke(uid: String): Resource<User> =
            repository.getSingleUser(uid)
}
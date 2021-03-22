/*
 * Created by Karic Kenan on 8.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.FollowResponseBody
import io.aethibo.fireshare.framework.utils.Resource

interface FollowUserUseCase {
    suspend operator fun invoke(uid: String): Resource<FollowResponseBody>
}

class FollowUserUseCaseImpl(private val repository: MainRepository) : FollowUserUseCase {

    override suspend fun invoke(uid: String): Resource<FollowResponseBody> =
            repository.toggleFollowForUser(uid)
}
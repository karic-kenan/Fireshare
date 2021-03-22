/*
 * Created by Karic Kenan on 10.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.FollowResponseBody
import io.aethibo.fireshare.framework.utils.Resource

interface CheckIsFollowingUseCase {
    suspend operator fun invoke(uid: String): Resource<FollowResponseBody>
}

class CheckIsFollowingUseCaseImpl(private val repository: MainRepository) : CheckIsFollowingUseCase {

    override suspend fun invoke(uid: String): Resource<FollowResponseBody> =
            repository.checkIfFollowing(uid)
}
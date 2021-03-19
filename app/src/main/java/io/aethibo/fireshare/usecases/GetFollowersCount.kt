/*
 * Created by Karic Kenan on 19.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.framework.utils.Resource

interface GetFollowersCountUseCase {
    suspend operator fun invoke(uid: String): Resource<Int>
}

class GetFollowersCountUseCaseImpl(private val repository: MainRepository) : GetFollowersCountUseCase {

    override suspend fun invoke(uid: String): Resource<Int> =
            repository.getFollowersCount(uid)
}
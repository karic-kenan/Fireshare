/*
 * Created by Karic Kenan on 22.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.framework.utils.Resource

interface FeedRemoveFollowUseCase {
    suspend operator fun invoke(ownerId: String): Resource<Any>
}

class FeedRemoveFollowUseCaseImpl(private val repository: MainRepository) : FeedRemoveFollowUseCase {

    override suspend fun invoke(ownerId: String): Resource<Any> =
            repository.removeFollowFromFeed(ownerId)
}
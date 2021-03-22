/*
 * Created by Karic Kenan on 22.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.framework.utils.Resource

interface FeedAddFollowUseCase {
    suspend operator fun invoke(ownerId: String): Resource<Any>
}

class FeedAddFollowUseCaseImpl(private val repository: MainRepository) : FeedAddFollowUseCase {

    override suspend fun invoke(ownerId: String): Resource<Any> =
            repository.addFollowToFeed(ownerId)
}
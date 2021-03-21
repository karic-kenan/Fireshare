/*
 * Created by Karic Kenan on 21.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.framework.utils.Resource

interface FeedRemoveLikeUseCase {
    suspend operator fun invoke(ownerId: String, postId: String): Resource<Any>
}

class FeedRemoveLikeUseCaseImpl(private val repository: MainRepository) : FeedRemoveLikeUseCase {

    override suspend fun invoke(ownerId: String, postId: String): Resource<Any> =
            repository.removeLikeFromFeed(ownerId, postId)
}
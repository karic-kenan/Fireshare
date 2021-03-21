/*
 * Created by Karic Kenan on 21.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.framework.utils.Resource

interface FeedAddLikeUseCase {
    suspend operator fun invoke(ownerId: String, postId: String, postImage: String): Resource<Any>
}

class FeedAddLikeUseCaseImpl(private val repository: MainRepository) : FeedAddLikeUseCase {

    override suspend fun invoke(ownerId: String, postId: String, postImage: String): Resource<Any> =
            repository.addLikeToFeed(ownerId, postId, postImage)
}
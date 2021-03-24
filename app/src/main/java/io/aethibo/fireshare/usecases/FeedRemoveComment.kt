/*
 * Created by Karic Kenan on 24.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.framework.utils.Resource

interface FeedRemoveCommentUseCase {
    suspend operator fun invoke(ownerId: String, commentId: String): Resource<Any>
}

class FeedRemoveCommentUseCaseImpl(private val repository: MainRepository) : FeedRemoveCommentUseCase {

    override suspend fun invoke(ownerId: String, commentId: String): Resource<Any> =
            repository.removeCommentFromFeed(ownerId, commentId)
}
/*
 * Created by Karic Kenan on 23.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.framework.utils.Resource

interface FeedAddCommentUseCase {
    suspend operator fun invoke(postId: String, commentId: String, ownerId: String, comment: String, postImage: String): Resource<Any>
}

class FeedAddCommentUseCaseImpl(private val repository: MainRepository) : FeedAddCommentUseCase {

    override suspend fun invoke(postId: String, commentId: String, ownerId: String, comment: String, postImage: String): Resource<Any> =
            repository.addCommentToFeed(postId, commentId, ownerId, comment, postImage)}
/*
 * Created by Karic Kenan on 21.3.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.framework.utils.Resource

interface GetSinglePostUseCase {
    suspend operator fun invoke(postId: String): Resource<Post>
}

class GetSinglePostUseCaseImpl(private val repository: MainRepository) : GetSinglePostUseCase {

    override suspend fun invoke(postId: String): Resource<Post> =
            repository.getSinglePost(postId)
}
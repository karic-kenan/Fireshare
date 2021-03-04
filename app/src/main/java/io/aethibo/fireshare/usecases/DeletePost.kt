/*
 * Created by Karic Kenan on 2.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.framework.utils.Resource

interface DeletePostUseCase {
    suspend operator fun invoke(body: Post): Resource<Post>
}

class DeletePostUseCaseImpl(private val repository: MainRepository) : DeletePostUseCase {

    override suspend fun invoke(body: Post): Resource<Post> =
            repository.deletePost(body)
}
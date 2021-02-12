/*
 * Created by Karic Kenan on 11.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.framework.utils.Resource

interface LikePostUseCase {
    suspend operator fun invoke(post: Post): Resource<Boolean>
}

class LikePostUseCaseImpl(private val repository: MainRepository) : LikePostUseCase {
    override suspend fun invoke(post: Post): Resource<Boolean> =
            repository.toggleLikeForPost(post)
}
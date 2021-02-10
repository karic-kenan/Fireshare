/*
 * Created by Karic Kenan on 2.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.request.PostRequestBody
import io.aethibo.fireshare.framework.utils.Resource

interface CreatePostUseCase {
    suspend operator fun invoke(body: PostRequestBody): Resource<Any>
}

class CreatePostUseCaseImpl(private val repository: MainRepository) : CreatePostUseCase {

    override suspend fun invoke(body: PostRequestBody): Resource<Any> =
            repository.createPost(body)
}
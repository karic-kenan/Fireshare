/*
 * Created by Karic Kenan on 2.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.PostToUpdateBody
import io.aethibo.fireshare.framework.utils.Resource

interface UpdatePostUseCase {
    suspend operator fun invoke(body: PostToUpdateBody): Resource<Any>
}

class UpdatePostUseCaseImpl(private val repository: MainRepository) : UpdatePostUseCase {

    override suspend fun invoke(body: PostToUpdateBody): Resource<Any> =
            repository.updatePost(body)
}
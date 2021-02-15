/*
 * Created by Karic Kenan on 15.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.Comment
import io.aethibo.fireshare.framework.utils.Resource

interface GetCommentsForPostUseCase {
    suspend operator fun invoke(postId: String): Resource<List<Comment>>
}

class GetCommentsForPostUseCaseImpl(private val repository: MainRepository) : GetCommentsForPostUseCase {

    override suspend operator fun invoke(postId: String) =
            repository.getCommentsForPost(postId)
}
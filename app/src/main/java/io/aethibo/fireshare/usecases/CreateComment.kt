/*
 * Created by Karic Kenan on 15.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.Comment
import io.aethibo.fireshare.framework.utils.Resource

interface CreateCommentUseCase {
    suspend operator fun invoke(postId: String, comment: String): Resource<Comment>
}

class CreateCommentUseCaseImpl(private val repository: MainRepository) : CreateCommentUseCase {

    override suspend operator fun invoke(postId: String, comment: String) =
            repository.createComment(postId, comment)
}
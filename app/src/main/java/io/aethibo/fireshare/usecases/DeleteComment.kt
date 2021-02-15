/*
 * Created by Karic Kenan on 15.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.usecases

import io.aethibo.fireshare.data.remote.main.MainRepository
import io.aethibo.fireshare.domain.Comment
import io.aethibo.fireshare.framework.utils.Resource

interface DeleteCommentUseCase {
    suspend operator fun invoke(comment: Comment): Resource<Comment>
}

class DeleteCommentUseCaseImpl(private val repository: MainRepository) : DeleteCommentUseCase {

    override suspend operator fun invoke(comment: Comment) =
            repository.deleteComment(comment)
}
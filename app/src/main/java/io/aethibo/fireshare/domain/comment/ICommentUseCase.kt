package io.aethibo.fireshare.domain.comment

import io.aethibo.fireshare.core.entities.Comment
import io.aethibo.fireshare.core.utils.Resource

interface ICommentUseCase {

    suspend fun getCommentsForPost(postId: String): Resource<List<Comment>>

    suspend fun createComment(postId: String, comment: String): Resource<Comment>

    suspend fun deleteComment(comment: Comment): Resource<Comment>
}
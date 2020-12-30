package io.aethibo.fireshare.domain.comment

import io.aethibo.fireshare.core.data.repositories.main.MainRepository
import io.aethibo.fireshare.core.entities.Comment
import io.aethibo.fireshare.core.utils.Resource

class CommentUseCase(private val repository: MainRepository) : ICommentUseCase {

    override suspend fun getCommentsForPost(postId: String): Resource<List<Comment>> =
            repository.getCommentsForPost(postId)

    override suspend fun createComment(postId: String, comment: String): Resource<Comment> =
            repository.createComment(postId, comment)

    override suspend fun deleteComment(comment: Comment): Resource<Comment> =
            repository.deleteComment(comment)
}
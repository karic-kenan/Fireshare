package io.aethibo.fireshare.domain.post

import io.aethibo.fireshare.core.entities.Post
import io.aethibo.fireshare.core.entities.PostToUpdate
import io.aethibo.fireshare.core.utils.Resource

interface IPostUseCase {
    suspend fun updatePost(postToUpdate: PostToUpdate): Resource<Any>

    suspend fun deletePost(post: Post): Resource<Post>

    suspend fun toggleLikeForPost(post: Post): Resource<Boolean>
}
package io.aethibo.fireshare.domain.post

import io.aethibo.fireshare.core.data.repositories.main.MainRepository
import io.aethibo.fireshare.core.entities.Post
import io.aethibo.fireshare.core.entities.PostToUpdate
import io.aethibo.fireshare.core.utils.Resource

class PostUseCase(private val repository: MainRepository) : IPostUseCase {
    override suspend fun updatePost(postToUpdate: PostToUpdate): Resource<Any> =
            repository.updatePost(postToUpdate)

    override suspend fun deletePost(post: Post): Resource<Post> =
            repository.deletePost(post)

    override suspend fun toggleLikeForPost(post: Post): Resource<Boolean> =
            repository.toggleLikeForPost(post)
}
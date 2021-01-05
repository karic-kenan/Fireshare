package io.aethibo.fireshare.core.data.repositories.main

import android.net.Uri
import io.aethibo.fireshare.core.entities.*
import io.aethibo.fireshare.core.utils.Resource

interface MainRepository {
    suspend fun createPost(imageUri: Uri, text: String): Resource<Any>

    suspend fun getSingleUser(uid: String): Resource<User>

    suspend fun updateProfile(profileUpdate: ProfileUpdate): Resource<Any>

    suspend fun updateProfilePicture(uid: String, imageUri: Uri): Uri?

    suspend fun updatePost(postToUpdate: PostToUpdate): Resource<Any>

    suspend fun deletePost(post: Post): Resource<Post>

    suspend fun toggleLikeForPost(post: Post): Resource<Boolean>

    suspend fun getCommentsForPost(postId: String): Resource<List<Comment>>

    suspend fun createComment(postId: String, comment: String): Resource<Comment>

    suspend fun deleteComment(comment: Comment): Resource<Comment>

    suspend fun updateComment(commentToUpdate: CommentToUpdate): Resource<Any>

    suspend fun searchUser(query: String): Resource<List<User>>
}
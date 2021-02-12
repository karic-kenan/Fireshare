/*
 * Created by Karic Kenan on 2.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.data.remote.main

import io.aethibo.fireshare.domain.Post
import io.aethibo.fireshare.domain.PostToUpdateBody
import io.aethibo.fireshare.domain.User
import io.aethibo.fireshare.domain.request.PostRequestBody
import io.aethibo.fireshare.domain.request.ProfileUpdateRequestBody
import io.aethibo.fireshare.framework.utils.Resource

interface MainRepository {

    // Posts
    suspend fun getPostsForProfile(uid: String): Resource<List<Post>>
    suspend fun createPost(body: PostRequestBody): Resource<Any>
    suspend fun updatePost(body: PostToUpdateBody): Resource<Any>
    suspend fun deletePost(post: Post): Resource<Post>
    suspend fun toggleLikeForPost(post: Post): Resource<Boolean>

    // User
    suspend fun getSingleUser(uid: String): Resource<User>
    suspend fun updateUserProfile(body: ProfileUpdateRequestBody): Resource<Any>
}
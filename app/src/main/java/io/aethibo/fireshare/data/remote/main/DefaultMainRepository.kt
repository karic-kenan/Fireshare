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

class DefaultMainRepository(private val mainRemote: MainRemoteDataSource) : MainRepository {

    /**
     * Post handler
     */
    override suspend fun getPostsForProfile(uid: String): Resource<List<Post>> =
            mainRemote.getPostsForProfile(uid)

    override suspend fun createPost(body: PostRequestBody): Resource<Any> =
            mainRemote.createPost(body)

    override suspend fun updatePost(body: PostToUpdateBody): Resource<Any> =
            mainRemote.updatePost(body)

    override suspend fun deletePost(post: Post): Resource<Post> =
            mainRemote.deletePost(post)

    /**
     * User handler
     */
    override suspend fun getSingleUser(uid: String): Resource<User> =
            mainRemote.getSingleUser(uid)

    override suspend fun updateUserProfile(body: ProfileUpdateRequestBody): Resource<Any> =
            mainRemote.updateUserProfile(body)
}
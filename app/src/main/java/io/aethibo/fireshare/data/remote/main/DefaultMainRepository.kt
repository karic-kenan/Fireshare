/*
 * Created by Karic Kenan on 2.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.data.remote.main

import io.aethibo.fireshare.domain.Comment
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

    override suspend fun toggleLikeForPost(post: Post): Resource<Boolean> =
            mainRemote.toggleLikeForPost(post)

    override suspend fun getTimeline(): Resource<List<Post>> =
        mainRemote.getTimeline()

    /**
     * User handler
     */
    override suspend fun getSingleUser(uid: String): Resource<User> =
            mainRemote.getSingleUser(uid)

    override suspend fun updateUserProfile(body: ProfileUpdateRequestBody): Resource<Any> =
            mainRemote.updateUserProfile(body)

    override suspend fun searchUsers(query: String): Resource<List<User>> =
            mainRemote.searchUsers(query)

    override suspend fun toggleFollowForUser(uid: String): Resource<Boolean> =
            mainRemote.toggleFollowForUser(uid)

    override suspend fun checkIfFollowing(uid: String): Resource<Boolean> =
            mainRemote.checkIfFollowing(uid)

    /**
     * Comments handler
     */
    override suspend fun getCommentsForPost(postId: String): Resource<List<Comment>> =
            mainRemote.getCommentsForPost(postId)

    override suspend fun createComment(postId: String, comment: String): Resource<Comment> =
            mainRemote.createComment(postId, comment)

    override suspend fun deleteComment(comment: Comment): Resource<Comment> =
            mainRemote.deleteComment(comment)
}
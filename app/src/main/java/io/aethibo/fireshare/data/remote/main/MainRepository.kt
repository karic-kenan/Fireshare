/*
 * Created by Karic Kenan on 2.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.data.remote.main

import io.aethibo.fireshare.domain.*
import io.aethibo.fireshare.domain.request.PostRequestBody
import io.aethibo.fireshare.domain.request.ProfileUpdateRequestBody
import io.aethibo.fireshare.framework.utils.Resource

interface MainRepository {

    /**
     * Posts
     */
    suspend fun getPostsForProfile(uid: String): Resource<List<Post>>
    suspend fun createPost(body: PostRequestBody): Resource<Any>
    suspend fun updatePost(body: PostToUpdateBody): Resource<Any>
    suspend fun deletePost(post: Post): Resource<Post>
    suspend fun toggleLikeForPost(post: Post): Resource<Boolean>
    suspend fun getTimeline(): Resource<List<Post>>
    suspend fun getPostsCount(uid: String): Resource<Int>
    suspend fun getSinglePost(postId: String): Resource<Post>

    /**
     * User
     */
    suspend fun getSingleUser(uid: String): Resource<User>
    suspend fun updateUserProfile(body: ProfileUpdateRequestBody): Resource<Any>
    suspend fun searchUsers(query: String): Resource<List<User>>
    suspend fun toggleFollowForUser(uid: String): Resource<FollowResponseBody>
    suspend fun checkIfFollowing(uid: String): Resource<FollowResponseBody>
    suspend fun getFollowingCount(uid: String): Resource<Int>
    suspend fun getFollowersCount(uid: String): Resource<Int>

    /**
     * Comments
     */
    suspend fun getCommentsForPost(postId: String): Resource<List<Comment>>
    suspend fun createComment(postId: String, comment: String): Resource<Comment>
    suspend fun deleteComment(comment: Comment): Resource<Comment>

    /**
     * Notifications feed
     */
    suspend fun addLikeToFeed(ownerId: String, postId: String, postImage: String): Resource<Any>
    suspend fun removeLikeFromFeed(ownerId: String, postId: String): Resource<Any>
    suspend fun addCommentToFeed(postId: String, commentId: String, ownerId: String, comment: String, postImage: String): Resource<Any>
    suspend fun removeCommentFromFeed(postId: String, ownerId: String, commentId: String): Resource<Any>
    suspend fun addFollowToFeed(ownerId: String): Resource<Any>
    suspend fun removeFollowFromFeed(ownerId: String): Resource<Any>
}
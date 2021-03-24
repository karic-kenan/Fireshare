/*
 * Created by Karic Kenan on 2.2.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fireshare.data.remote.main

import io.aethibo.fireshare.domain.*
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

    override suspend fun getPostsCount(uid: String): Resource<Int> =
            mainRemote.getPostsCount(uid)

    /**
     * User handler
     */
    override suspend fun getSingleUser(uid: String): Resource<User> =
            mainRemote.getSingleUser(uid)

    override suspend fun updateUserProfile(body: ProfileUpdateRequestBody): Resource<Any> =
            mainRemote.updateUserProfile(body)

    override suspend fun searchUsers(query: String): Resource<List<User>> =
            mainRemote.searchUsers(query)

    override suspend fun toggleFollowForUser(uid: String): Resource<FollowResponseBody> =
            mainRemote.toggleFollowForUser(uid)

    override suspend fun checkIfFollowing(uid: String): Resource<FollowResponseBody> =
            mainRemote.checkIfFollowing(uid)

    override suspend fun getFollowingCount(uid: String): Resource<Int> =
            mainRemote.getFollowingCount(uid)

    override suspend fun getFollowersCount(uid: String): Resource<Int> =
            mainRemote.getFollowersCount(uid)

    /**
     * Comments handler
     */
    override suspend fun getCommentsForPost(postId: String): Resource<List<Comment>> =
            mainRemote.getCommentsForPost(postId)

    override suspend fun createComment(postId: String, comment: String): Resource<Comment> =
            mainRemote.createComment(postId, comment)

    override suspend fun deleteComment(comment: Comment): Resource<Comment> =
            mainRemote.deleteComment(comment)

    /**
     * Notifications handler
     */
    override suspend fun addLikeToFeed(ownerId: String, postId: String, postImage: String): Resource<Any> =
            mainRemote.addLikeToFeed(ownerId, postId, postImage)

    override suspend fun removeLikeFromFeed(ownerId: String, postId: String): Resource<Any> =
            mainRemote.removeLikeFromFeed(ownerId, postId)

    override suspend fun addCommentToFeed(postId: String, commentId: String, ownerId: String, comment: String, postImage: String): Resource<Any> =
            mainRemote.addCommentToFeed(postId, commentId, ownerId, comment, postImage)

    override suspend fun removeCommentFromFeed(ownerId: String, commentId: String): Resource<Any> =
            mainRemote.removeCommentFromFeed(ownerId, commentId)

    override suspend fun addFollowToFeed(ownerId: String): Resource<Any> =
            mainRemote.addFollowToFeed(ownerId)

    override suspend fun removeFollowFromFeed(ownerId: String): Resource<Any> =
            mainRemote.removeFollowFromFeed(ownerId)

    override suspend fun getNotificationFeed(): Resource<List<ActivityFeedItem>> =
            mainRemote.getNotificationFeed()
}
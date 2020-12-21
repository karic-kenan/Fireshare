package io.aethibo.fireshare.core.utils

object AppConst {

    /**
     * Main collections
     */
    const val usersCollection: String = "users"
    const val postsCollection: String = "posts"

    /**
     * Sub-collections
     */
    const val usersPostsCollection: String = "userPosts"

    const val MAX_USERNAME_LENGTH = 8
    const val MIN_USERNAME_LENGTH = 3
    const val MIN_PASSWORD_LENGTH = 5

    const val PAGE_SIZE = 5

    const val DEFAULT_PROFILE_PICTURE_URL =
            "https://firebasestorage.googleapis.com/v0/b/fireshare-b942b.appspot.com/o/img_avatar.png?alt=media&token=ad03014d-d81e-4691-9b84-8e3a4548ceb7"
}
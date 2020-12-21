package io.aethibo.fireshare.core.data.repositories.main

import android.net.Uri
import io.aethibo.fireshare.core.entities.ProfileUpdate
import io.aethibo.fireshare.core.entities.User
import io.aethibo.fireshare.core.utils.Resource

interface MainRepository {
    suspend fun createPost(imageUri: Uri, text: String): Resource<Any>

    suspend fun getSingleUser(uid: String): Resource<User>

    suspend fun updateProfile(profileUpdate: ProfileUpdate): Resource<Any>

    suspend fun updateProfilePicture(uid: String, imageUri: Uri): Uri?
}
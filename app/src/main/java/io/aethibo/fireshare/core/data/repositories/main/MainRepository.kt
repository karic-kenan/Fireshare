package io.aethibo.fireshare.core.data.repositories.main

import android.net.Uri
import io.aethibo.fireshare.core.utils.Resource

interface MainRepository {
    suspend fun createPost(imageUri: Uri, text: String): Resource<Any>
}
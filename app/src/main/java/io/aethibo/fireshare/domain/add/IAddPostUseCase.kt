package io.aethibo.fireshare.domain.add

import android.net.Uri
import io.aethibo.fireshare.core.utils.Resource

interface IAddPostUseCase {
    suspend fun createPost(imageUri: Uri, text: String): Resource<Any>
}
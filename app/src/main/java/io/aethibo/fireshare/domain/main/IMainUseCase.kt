package io.aethibo.fireshare.domain.main

import android.net.Uri
import io.aethibo.fireshare.core.utils.Resource

interface IMainUseCase {
    suspend fun createPost(imageUri: Uri, text: String): Resource<Any>
}
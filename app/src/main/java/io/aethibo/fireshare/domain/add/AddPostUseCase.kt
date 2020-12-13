package io.aethibo.fireshare.domain.add

import android.net.Uri
import io.aethibo.fireshare.core.data.repositories.main.MainRepository
import io.aethibo.fireshare.core.utils.Resource

class AddPostUseCase(private val repository: MainRepository) : IAddPostUseCase {

    override suspend fun createPost(imageUri: Uri, text: String): Resource<Any> =
            repository.createPost(imageUri, text)
}
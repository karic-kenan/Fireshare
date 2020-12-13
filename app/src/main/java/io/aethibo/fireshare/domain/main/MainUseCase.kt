package io.aethibo.fireshare.domain.main

import android.net.Uri
import io.aethibo.fireshare.core.data.repositories.main.MainRepository
import io.aethibo.fireshare.core.utils.Resource

class MainUseCase(private val repository: MainRepository) : IMainUseCase {

    override suspend fun createPost(imageUri: Uri, text: String): Resource<Any> =
            repository.createPost(imageUri, text)
}
package io.aethibo.fireshare.domain

data class PostToUpdateBody(
        val postIdToUpdate: String = "",
        val caption: String?
)
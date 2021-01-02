package io.aethibo.fireshare.core.entities

data class CommentToUpdate(
        val commentIdToUpdate: String = "",
        val postId: String = "",
        val comment: String = ""
)

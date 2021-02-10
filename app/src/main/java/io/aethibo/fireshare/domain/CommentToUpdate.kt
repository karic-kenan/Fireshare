package io.aethibo.fireshare.domain

data class CommentToUpdate(
        val commentIdToUpdate: String = "",
        val postId: String = "",
        val comment: String = ""
)

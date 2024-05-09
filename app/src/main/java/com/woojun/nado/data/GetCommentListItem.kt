package com.woojun.nado.data

data class GetCommentListItem(
    val commentID: Int,
    val content: String,
    val id: Int,
    val postID: Int
)
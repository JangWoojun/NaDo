package com.woojun.nado.data

data class Lecture(
    val titleText: String,
    val subText: String,
    val image: String,
    val url: String,
    val isNotPopularity: Boolean,
    val category: String
)

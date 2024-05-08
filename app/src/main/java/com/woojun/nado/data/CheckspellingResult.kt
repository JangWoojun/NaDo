package com.woojun.nado.data

data class CheckspellingResult(
    val changes: List<Change>,
    val corrected: String,
    val original: String
)
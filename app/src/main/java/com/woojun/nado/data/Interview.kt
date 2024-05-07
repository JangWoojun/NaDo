package com.woojun.nado.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Interview(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val question: String,
    val content: String,
    var isSelected: Boolean = false
)

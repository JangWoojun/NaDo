package com.woojun.nado.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AiInterview(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,
    val date: String,
    var isSelected: Boolean = false
)

package com.woojun.nado.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Resume(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val content: String,
    var isSelected: Boolean = false
)
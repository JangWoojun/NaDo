package com.woojun.nado.database

import androidx.room.*
import com.woojun.nado.data.Resume

@Dao
interface ResumeDAO {
    @Insert
    fun insertResume(resume: Resume)

    @Update
    fun updateResume(resume: Resume)

    @Query("SELECT * FROM Resume")
    fun getResumeList(): MutableList<Resume>

    @Delete
    fun deleteResume(resume: Resume)
}
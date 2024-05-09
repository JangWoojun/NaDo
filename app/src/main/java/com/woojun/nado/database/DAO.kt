package com.woojun.nado.database

import androidx.room.*
import com.woojun.nado.data.AiInterview
import com.woojun.nado.data.Interview
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

@Dao
interface InterviewDAO {
    @Insert
    fun insertInterview(interview: Interview)

    @Update
    fun updateInterview(interview: Interview)

    @Query("SELECT * FROM Interview")
    fun getInterviewList(): MutableList<Interview>

    @Delete
    fun deleteInterview(interview: Interview)
}

@Dao
interface AiInterviewDAO {
    @Insert
    fun insertAiInterview(aiInterview: AiInterview)

    @Update
    fun updateAiInterview(aiInterview: AiInterview)

    @Query("SELECT * FROM AiInterview")
    fun getAiInterviewList(): MutableList<AiInterview>

    @Delete
    fun deleteAiInterview(aiInterview: AiInterview)
}
package com.woojun.nado.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.woojun.nado.data.AiInterview
import com.woojun.nado.data.Interview
import com.woojun.nado.data.Resume

class TypeConverter {
    @TypeConverter
    fun fromResumeList(resumeList: MutableList<Resume>): String {
        return Gson().toJson(resumeList)
    }

    @TypeConverter
    fun toResumeList(resumeListString: String):MutableList<Resume> {
        val listType = object : TypeToken<MutableList<Resume>>() {}.type
        return Gson().fromJson(resumeListString, listType)
    }

    @TypeConverter
    fun fromInterviewList(interviewList: MutableList<Interview>): String {
        return Gson().toJson(interviewList)
    }

    @TypeConverter
    fun toInterviewList(interviewListString: String):MutableList<Interview> {
        val listType = object : TypeToken<MutableList<Interview>>() {}.type
        return Gson().fromJson(interviewListString, listType)
    }

    @TypeConverter
    fun fromAiInterviewList(aiInterviewList: MutableList<AiInterview>): String {
        return Gson().toJson(aiInterviewList)
    }

    @TypeConverter
    fun toAiInterviewList(aiInterviewListString: String):MutableList<AiInterview> {
        val listType = object : TypeToken<MutableList<AiInterview>>() {}.type
        return Gson().fromJson(aiInterviewListString, listType)
    }
}

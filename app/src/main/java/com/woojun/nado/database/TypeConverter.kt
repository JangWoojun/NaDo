package com.woojun.nado.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

}

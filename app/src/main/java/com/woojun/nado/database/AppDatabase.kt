package com.woojun.nado.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.woojun.nado.data.AiInterview
import com.woojun.nado.data.Interview
import com.woojun.nado.data.Resume

@TypeConverters(
    TypeConverter::class
)

@Database(entities = [Resume::class, Interview::class, AiInterview::class], version = 4)
abstract class AppDatabase: RoomDatabase() {
    abstract fun resumeDao(): ResumeDAO
    abstract fun interviewDao(): InterviewDAO
    abstract fun aiInterviewDao(): AiInterviewDAO



    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app database")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
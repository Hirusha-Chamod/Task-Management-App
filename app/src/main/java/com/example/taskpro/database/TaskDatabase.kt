package com.example.taskpro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskpro.converters.TypeConverter
import com.example.taskpro.dao.TaskDao
import com.example.taskpro.models.Task
@Database(
    entities = [Task::class],
    version = 2, // Increment the version number
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract val taskDao: TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_db"
                )
                    .fallbackToDestructiveMigration() // This will recreate the database on version mismatch
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}

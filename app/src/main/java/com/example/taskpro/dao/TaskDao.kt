package com.example.taskpro.dao

import android.app.Application
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskpro.models.Task
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task ORDER BY date DESC")
    fun getTaskList(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    // First way
    @Delete
    suspend fun deleteTask(task: Task) : Int




    @Query("UPDATE Task SET taskTitle=:title, description=:description, dueDate=:dueDate WHERE taskId=:taskId")
    suspend fun updateTaskPaticularField(taskId: String, title: String, description: String, dueDate: Date): Int



    @Query("SELECT * FROM Task WHERE taskTitle = :query")
    fun searchTaskList(query: String): Flow<List<Task>>


}
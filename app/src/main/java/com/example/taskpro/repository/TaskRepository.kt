package com.example.taskpro.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.taskpro.dao.TaskDao
import com.example.taskpro.database.TaskDatabase
import com.example.taskpro.models.Task
import com.example.taskpro.utils.Resource
import com.example.taskpro.utils.Resource.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.PriorityQueue


class TaskRepository(application: Application) {
    private val taskDao: TaskDao = TaskDatabase.getInstance(application).taskDao



    fun getTaskList() = flow {
        emit(Loading())
        try {
            val result = taskDao.getTaskList()
            emit(Success(result))
        }catch (e:Exception){
            emit(Error(e.message.toString()))
        }
    }


    fun insertTask(task: Task) = MutableLiveData<Resource<Long>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.insertTask(task)
                postValue(Success(result))
            }
        }catch (e: Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun deleteTask(task: Task) = MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.deleteTask(task)
                postValue(Success(result))
            }
        }catch (e: Exception){
            postValue(Error(e.message.toString()))
        }
    }



    fun updateTaskPaticularField(taskId: String,title:String,description:String,dueDate: Date) = MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.updateTaskPaticularField(taskId, title, description,dueDate)
                postValue(Success(result))
            }
        }catch (e: Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun searchTaskList(query: String,application: Application) = flow {
        emit(Loading())
        try {
            Toast.makeText(application,"Searching from repo",Toast.LENGTH_SHORT).show()
            val result = taskDao.searchTaskList(query)
            emit(Success(result))
        } catch (e: Exception) {
            emit(Error(e.message.toString()))
        }
    }




}
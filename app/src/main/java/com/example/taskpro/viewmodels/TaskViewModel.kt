package com.example.taskpro.viewmodels


import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.taskpro.dao.TaskDao
import com.example.taskpro.database.TaskDatabase
import com.example.taskpro.models.Task
import com.example.taskpro.repository.TaskRepository

import com.example.taskpro.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application)

    fun getTaskList() = taskRepository.getTaskList()

    fun insertTask(task: Task): MutableLiveData<Resource<Long>> {
        return taskRepository.insertTask(task)
    }

    fun deleteTask(task: Task): MutableLiveData<Resource<Int>> {
        return taskRepository.deleteTask(task)
    }


    fun updateTaskPaticularField(taskId: String,title:String,description:String,dueDate:Date): MutableLiveData<Resource<Int>> {
        return taskRepository.updateTaskPaticularField(taskId, title, description,dueDate)
    }

    fun searchTaskList(query: String,application: Application): Flow<List<Task>> {

        val taskDao: TaskDao = TaskDatabase.getInstance(application).taskDao
        Toast.makeText(application, "Search from view model " + query, Toast.LENGTH_SHORT).show()
        return taskDao.searchTaskList(query)

    }

}
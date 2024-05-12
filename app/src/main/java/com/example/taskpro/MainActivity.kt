package com.example.taskpro

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.taskpro.adapters.TaskRecyclerViewAdapter
import com.example.taskpro.databinding.ActivityMainBinding
import com.example.taskpro.models.Task
import com.example.taskpro.models.getDateFromDatePicker
import com.example.taskpro.utils.Status
import com.example.taskpro.utils.clearEditText
import com.example.taskpro.utils.longToastShow
import com.example.taskpro.utils.setupDialog
import com.example.taskpro.utils.validateEditText
import com.example.taskpro.viewmodels.TaskViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val addTaskDialog : Dialog by lazy {
        Dialog(this,R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.add_task_dialog)
        }
    }

    private val updateTaskDialog : Dialog by lazy {
        Dialog(this,R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.update_task_dialog)
        }
    }

    private val loadingDialog : Dialog by lazy {
        Dialog(this,R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.loading_dialog)
        }
    }

    private val taskViewModel: TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)
        enableEdgeToEdge()



        // Add task start
        val addCloseImg = addTaskDialog.findViewById<ImageView>(R.id.closeImg)
        addCloseImg.setOnClickListener { addTaskDialog.dismiss() }

        val addETTitle = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val addETTitleL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)
        val datePicker = addTaskDialog.findViewById<DatePicker>(R.id.date)


        addETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETTitle, addETTitleL)
            }

        })

        val addETDesc = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val addETDescL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)

        addETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETDesc, addETDescL)
            }
        })




        mainBinding.addTaskFABtn.setOnClickListener{
            clearEditText(addETTitle, addETTitleL)
            clearEditText(addETDesc, addETDescL)
            addTaskDialog.show()
        }
        val saveTaskBtn = addTaskDialog.findViewById<Button>(R.id.saveTaskBtn)
        saveTaskBtn.setOnClickListener {
            if (validateEditText(addETTitle, addETTitleL)
                && validateEditText(addETDesc, addETDescL)
            ) {
                val selectedDate = getDateFromDatePicker(datePicker)
                addTaskDialog.dismiss()
               val newTask = Task(
                UUID.randomUUID().toString().trim(),
                addETTitle.text.toString().trim(),
                addETDesc.text.toString().trim(),
                Date(),
                   selectedDate
               )
                taskViewModel.insertTask(newTask).observe(this){
                     when(it.status){
                         Status.LOADING->{
                             loadingDialog.show()
                         }
                         Status.SUCCESS->{
                             loadingDialog.dismiss()
                             if(it.data?.toInt()!=-1){
                                 longToastShow("Task added")
                                 }

                         }

                         Status.ERROR -> {
                             loadingDialog.dismiss()

                         }
                         }
                     }
            }
        }



        // Add task end


        // Update Task Start
        val updateETTitle = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val updateETTitleL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        updateETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETTitle, updateETTitleL)
            }

        })

        val updateETDesc = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val updateETDescL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)
        val updateDatePicker = updateTaskDialog.findViewById<DatePicker>(R.id.date)


        updateETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETDesc, updateETDescL)
            }
        })

        val updateCloseImg = updateTaskDialog.findViewById<ImageView>(R.id.closeImg)
        updateCloseImg.setOnClickListener { updateTaskDialog.dismiss() }

        val updateTaskBtn = updateTaskDialog.findViewById<Button>(R.id.updateTaskBtn)



       val taskRecyclerViewAdapter = TaskRecyclerViewAdapter(){type,position, task ->
           if (type =="delete"){
            taskViewModel.deleteTask(task).observe(this){
                when(it.status){
                    Status.LOADING->{
                        loadingDialog.show()
                    }
                    Status.SUCCESS->{
                        loadingDialog.dismiss()
                        if(it.data !=-1){
                            longToastShow("Task deleted")
                        }

                    }

                    Status.ERROR -> {
                        loadingDialog.dismiss()

                    }
                }

            }
           }else if (type == "update") {
               updateETTitle.setText(task.title)
               updateETDesc.setText(task.description)
               updateTaskBtn.setOnClickListener {
                   if (validateEditText(updateETTitle, updateETTitleL)
                       && validateEditText(updateETDesc, updateETDescL)
                   ) {
                       val updateSelectedDate = getDateFromDatePicker(updateDatePicker)

                       updateTaskDialog.dismiss()
                       loadingDialog.show()
                       taskViewModel

                           .updateTaskPaticularField(
                               task.id,
                               updateETTitle.text.toString().trim(),
                               updateETDesc.text.toString().trim(),
                               updateSelectedDate

                           )
                           .observe(this) {
                               when (it.status) {
                                   Status.LOADING -> {
                                       loadingDialog.show()
                                   }

                                   Status.SUCCESS -> {
                                       loadingDialog.dismiss()
                                       if (it.data != -1) {
                                           longToastShow("Task Updated Successfully")
                                       }
                                   }

                                   Status.ERROR -> {
                                       loadingDialog.dismiss()
                                       it.message?.let { it1 -> longToastShow(it1) }
                                   }
                               }
                           }
                   }
               }
               updateTaskDialog.show()
           }
        }

        mainBinding.taskRV.adapter = taskRecyclerViewAdapter

        callGetTaskList(taskRecyclerViewAdapter)

        callSearch()


    }


    private fun callSearch() {
        mainBinding.edSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(query: Editable) {
                if (query.toString().isNotEmpty()){
                    taskViewModel.searchTaskList(query.toString())
                }else{
                    taskViewModel.getTaskList()
                }
            }
        })

        mainBinding.edSearch.setOnEditorActionListener{ v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){

                return@setOnEditorActionListener true
            }else{
            false}
        }
    }


    private fun callGetTaskList(taskRecyclerViewAdapter: TaskRecyclerViewAdapter){
        CoroutineScope(Dispatchers.Main).launch{
        taskViewModel.getTaskList().collect{
            when(it.status){
                Status.LOADING->{
                    loadingDialog.show()
                }
                Status.SUCCESS->{
                    it.data?.collect{taskList->
                        loadingDialog.dismiss()
                        taskRecyclerViewAdapter.addAllTask(taskList)
                    }

                }

                Status.ERROR -> {
                    loadingDialog.dismiss()

                }
            }
        }
        }

        }




}

package com.example.taskpro.models

import android.widget.DatePicker
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date

@Entity()
data class Task(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "taskId")
    val id: String,
    @ColumnInfo(name = "taskTitle")
    val title: String,
    val description: String,
    val date: Date,
    val dueDate: Date

)

fun getDateFromDatePicker(datePicker: DatePicker): Date {
    val day = datePicker.dayOfMonth
    val month = datePicker.month
    val year = datePicker.year

    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)

    return calendar.time
}

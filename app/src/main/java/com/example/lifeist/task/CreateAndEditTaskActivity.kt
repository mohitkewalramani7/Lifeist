package com.example.lifeist.task

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import com.example.lifeist.R
import java.text.SimpleDateFormat
import java.util.*

class CreateAndEditTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener{

    private lateinit var taskDateButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_and_edit_task)
        val intentType = intent.getStringExtra(INTENT_TYPE)
        renderRelevantLayout(intentType)
        taskDateButton = findViewById(R.id.taskDateButton)
        setupDateButtonClickListener()
        cancelButtonSetup()
    }

    private fun renderRelevantLayout(intentType: String){
        if (intentType.equals(INTENT_TYPE_CREATE))
            renderCreateLayout()
        else
            renderEditLayout()
    }

    private fun renderCreateLayout(){
        // nothing
    }

    private fun renderEditLayout(){
        findViewById<TextView>(R.id.activityTitle).text = resources.getText(R.string.update_task)
        findViewById<MaterialButton>(R.id.createTask).text = resources.getText(R.string.update_task)
    }

    private fun setupDateButtonClickListener(){
        taskDateButton.setOnClickListener{
            val cal = Calendar.getInstance()
            val currentYear = cal.get(Calendar.YEAR)
            val currentMonth = cal.get(Calendar.MONTH)
            val currentDay = cal.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(it.context, this, currentYear, currentMonth, currentDay)
            datePickerDialog.show()
        }
    }

    private fun cancelButtonSetup(){
        findViewById<MaterialButton>(R.id.cancelTask).setOnClickListener{
            this.finish()
        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        taskDateButton.text = String.format("Task Due Date: %04d-%02d-%02d", year, month+1, dayOfMonth)
    }

    companion object {
        val INTENT_TYPE = "INTENT_TYPE"
        val INTENT_TYPE_CREATE = "CREATE_TASK"
        val INTENT_TYPE_UPDATE = "UPDATE_TASK"
        val INTENT_UPDATE_TASK_OBJECT = "UPDATE_TASK_OBJECT"
    }

}

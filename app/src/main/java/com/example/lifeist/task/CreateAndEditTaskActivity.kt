package com.example.lifeist.task

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lifeist.R
import com.example.lifeist.category.CategoryDisplayActivity.Companion.CATEGORY_KEY
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable
import java.util.*

class CreateAndEditTaskActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener{

    private lateinit var database: DatabaseReference
    private lateinit var taskDateButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_and_edit_task)
        val intentType = intent.getStringExtra(INTENT_TYPE)
        renderRelevantLayout(intentType)
        database = FirebaseDatabase.getInstance().reference
        taskDateButton = findViewById(R.id.taskDateButton)
        setupDateButtonClickListener()
        createButtonSetup()
        cancelButtonSetup()
    }

    private fun renderRelevantLayout(intentType: String){
        if (intentType.equals(INTENT_TYPE_UPDATE))
            renderEditLayout()
    }

    private fun renderEditLayout(){
        findViewById<TextView>(R.id.activityTitle).text = resources.getText(R.string.update_task)
        findViewById<MaterialButton>(R.id.createTask).text = resources.getText(R.string.update_task)

        findViewById<TextInputEditText>(R.id.taskTitleInputText)
            .setText(intent.getStringExtra(TASK_TITLE).toString())
        findViewById<MaterialButton>(R.id.taskDateButton)
            .text = intent.getStringExtra(TASK_DUE_DATE).toString()
        findViewById<TextInputEditText>(R.id.taskDescriptionInputText)
            .setText(intent.getStringExtra(TASK_DESCRIPTION).toString())
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

    private fun createButtonSetup(){
        findViewById<MaterialButton>(R.id.createTask).setOnClickListener{
            val taskTitle = getTaskTitle()
            if (taskTitle == false){
                return@setOnClickListener
            }
            val taskDueDate = getTaskDueDate()
            val taskDescription = getTaskDescription();

            val newTask = Task(
                taskTitle as String,
                taskDescription as String,
                taskDueDate
            )

            if (intent.getStringExtra(INTENT_TYPE) == INTENT_TYPE_CREATE)
                createTask(newTask)
            else
                updateTask(newTask)
        }
    }

    private fun getTaskTitle() : Any {
        val taskTitle = findViewById<TextInputEditText>(R.id.taskTitleInputText)
            .text.toString()
        if (taskTitle == ""){
            Toast.makeText(
                applicationContext,
                "Please Enter a Task Name",
                Toast.LENGTH_SHORT
            ).show()
            return false;
        }
        return taskTitle;
    }

    private fun getTaskDueDate() : String {
        var taskDueDate = findViewById<MaterialButton>(R.id.taskDateButton).text.toString()
        taskDueDate = taskDueDate.replace("Task Due Date: ", "")
        return taskDueDate
    }

    private fun getTaskDescription() : Any {
        val taskDescription =
            findViewById<TextInputEditText>(R.id.taskDescriptionInputText).text.toString();
        return taskDescription
    }

    private fun createTask(task: Task) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid;
        val categoryKey = intent.getStringExtra(CATEGORY_KEY)
        val newTaskReference = database.child(userId).child(categoryKey).push()
        val newTaskKey = newTaskReference.key
        database.child(userId)
            .child(categoryKey)
            .child("tasklist")
            .child(newTaskKey!!)
            .setValue(task)
            .addOnSuccessListener {
                Toast.makeText(
                    applicationContext,
                    "Task Added",
                    Toast.LENGTH_SHORT
                ).show()
                this.finish()
            }
            .addOnFailureListener{
                Toast.makeText(
                    applicationContext,
                    it.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun updateTask(newTask: Task){
        val userId = FirebaseAuth.getInstance().currentUser!!.uid;
        val categoryKey = intent.getStringExtra(CATEGORY_KEY)
        val taskKey = intent.getStringExtra(TASK_KEY)

        val taskUpdates: HashMap<String, Any> = HashMap()
        taskUpdates["/${userId}/${categoryKey}/tasklist/${taskKey}/title"] =
            newTask.title
        taskUpdates["/${userId}/${categoryKey}/tasklist/${taskKey}/dueDate"] =
            newTask.dueDate
        taskUpdates["/${userId}/${categoryKey}/tasklist/${taskKey}/description"] =
            newTask.description

        database.updateChildren(taskUpdates).addOnSuccessListener {
            Toast.makeText(applicationContext, "Task Updated", Toast.LENGTH_SHORT).show()
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

        val TASK_KEY = "TASK_KEY"
        val TASK_TITLE = "TASK_TITLE"
        val TASK_DUE_DATE = "TASK_DUE_DATE"
        val TASK_DESCRIPTION = "TASK_DESCRIPTION"
    }

}

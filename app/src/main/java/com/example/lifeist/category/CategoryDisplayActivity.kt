package com.example.lifeist.category

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.lifeist.R
import com.example.lifeist.task.CreateAndEditTaskActivity
import com.example.lifeist.task.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CategoryDisplayActivity : AppCompatActivity() {

    val UPDATE_ID_VAL = 0;

    private lateinit var categoryKey: String;
    private lateinit var taskList: ListView;
    private lateinit var selectedTask: Task;
    private lateinit var addTaskButton: FloatingActionButton;
    private lateinit var bottomSheetTaskViewer: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_display)
        renderCategoryDetails(
            intent.getStringExtra(CATEGORY_TITLE),
            intent.getStringExtra(CATEGORY_DESCRIPTION)
        )
        categoryKey = intent.getStringExtra(CATEGORY_KEY)
        initializeTaskListview()
        setAddTaskButtonClickListener()
        initializeBottomSheetTaskViewer()
    }

    private fun renderCategoryDetails(categoryTitle: String, categoryDescription: String){
        findViewById<TextView>(R.id.enteredCategoryTitle).text = categoryTitle
        findViewById<TextView>(R.id.enteredCategoryDescription).text = categoryDescription
    }

    private fun initializeTaskListview(){
        taskList = findViewById(R.id.taskListview)
        taskList.adapter = populateTasksList()
        setTaskListItemClickListener()
    }

    private fun populateTasksList(): TaskReadAdapter {
        val tasks = TaskReadAdapter()
        val tasksParsed
                = intent.getSerializableExtra("tasklist")
        val tasksList = tasksParsed as HashMap<String, String>
        tasks.taskListItems = convertHashmaptoTasklist(tasksList)
        return tasks
    }

    private fun convertHashmaptoTasklist(taskList: HashMap<String, String>): ArrayList<Task>{
        val parsedTaskList = ArrayList<Task>()
        taskList.keys.forEach{
            val taskData = taskList[it] as HashMap<String, String>
            val task = Task(
                taskData.get("title")!!,
                taskData.get("description")!!,
                taskData.get("dueDate")!!,
                it
            )
            parsedTaskList.add(task)
        }
        return parsedTaskList
    }

    private fun setTaskListItemClickListener(){
        taskList.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val clickedTask = adapterView.adapter.getItem(i) as Task
            selectedTask = clickedTask
            Log.d("MAYBEID?", "ID: " + clickedTask.id)
            setDropdownDetails(clickedTask)
            if (bottomSheetTaskViewer.state == BottomSheetBehavior.STATE_HIDDEN)
                bottomSheetTaskViewer.state = BottomSheetBehavior.STATE_EXPANDED
            else
                bottomSheetTaskViewer.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun setDropdownDetails(clickedTask: Task){
        findViewById<TextView>(R.id.dropdownTaskName).text = clickedTask.title
        findViewById<TextView>(R.id.dropdownDueDate).text = clickedTask.dueDate
        findViewById<TextView>(R.id.dropdownDescription).text = clickedTask.description
    }

    private fun setAddTaskButtonClickListener(){
        addTaskButton = findViewById(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            showAddTaskActivity()
        }
    }

    private fun showAddTaskActivity(){
        val intent = Intent(this, CreateAndEditTaskActivity::class.java)
        intent.putExtra(
            CreateAndEditTaskActivity.INTENT_TYPE,
            CreateAndEditTaskActivity.INTENT_TYPE_CREATE
        )
        intent.putExtra(
            CATEGORY_KEY,
            categoryKey
        )
        startActivity(intent)
    }

    private fun initializeBottomSheetTaskViewer(){
        val categoryDisplayRoot = findViewById<LinearLayout>(R.id.taskViewBottomSheetRoot)
        bottomSheetTaskViewer = BottomSheetBehavior.from(categoryDisplayRoot)
        bottomSheetTaskViewer.peekHeight = 500
        bottomSheetTaskViewer.isHideable = true
        bottomSheetTaskViewer.state = BottomSheetBehavior.STATE_HIDDEN
        initializeUpdateTaskButton(categoryDisplayRoot.findViewById(R.id.taskUpdateButton))
        initializeDeleteTaskButton(categoryDisplayRoot.findViewById(R.id.taskDeleteButton))
    }

    private fun initializeUpdateTaskButton(updateTaskButton: Button){
        updateTaskButton.setOnClickListener{
            val intent = Intent(this, CreateAndEditTaskActivity::class.java)
            intent.putExtra(
                CreateAndEditTaskActivity.INTENT_TYPE,
                CreateAndEditTaskActivity.INTENT_TYPE_UPDATE
            )
            intent.putExtra(
                CreateAndEditTaskActivity.TASK_TITLE,
                selectedTask.title
            )
            intent.putExtra(
                CreateAndEditTaskActivity.TASK_DUE_DATE,
                selectedTask.dueDate
            )
            intent.putExtra(
                CreateAndEditTaskActivity.TASK_DESCRIPTION,
                selectedTask.description
            )
            intent.putExtra(
                CATEGORY_KEY,
                categoryKey
            )
            intent.putExtra(
                CreateAndEditTaskActivity.TASK_KEY,
                selectedTask.id
            )
            startActivity(intent)
        }
    }

    private fun initializeDeleteTaskButton(deleteTaskButton: Button){
        deleteTaskButton.setOnClickListener{
            val deleteFragment =
                DeleteCategoryAndTaskDialogFragment(categoryKey, selectedTask.id)
            deleteFragment.deleteType = DELETE_TYPE_TASK
            deleteFragment.show(supportFragmentManager, "delete_task")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.category_menu, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            R.id.category_edit_button -> {
                startUpdateCategoryActivity()
            }
            R.id.category_delete_button -> {
                showDeleteCategoryConfirmationPopup()
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun startUpdateCategoryActivity() : Boolean{
        val updateIntent = Intent(this, CreateAndEditCategoryActivity::class.java)
        updateIntent.putExtra(
            CreateAndEditCategoryActivity.INTENT_TYPE,
            CreateAndEditCategoryActivity.EDIT_CATEGORY
        )
        updateIntent.putExtra(
            CATEGORY_KEY,
            intent.getStringExtra(CATEGORY_KEY)
        )
        updateIntent.putExtra(
            CATEGORY_TITLE,
            intent.getStringExtra(CATEGORY_TITLE)
        )
        updateIntent.putExtra(
            CATEGORY_DESCRIPTION,
            intent.getStringExtra(CATEGORY_DESCRIPTION)
        )
        startActivityForResult(updateIntent, UPDATE_ID_VAL)
        return true
    }

    private fun showDeleteCategoryConfirmationPopup(): Boolean{
        val categoryKey = intent.getStringExtra(CATEGORY_KEY)
        val deleteFragment = DeleteCategoryAndTaskDialogFragment(categoryKey)
        deleteFragment.deleteType = DELETE_TYPE_CATEGORY
        deleteFragment.show(supportFragmentManager, "delete_category")
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            UPDATE_ID_VAL -> {
                renderCategoryDetails(
                    data!!.getStringExtra(CATEGORY_TITLE),
                    data.getStringExtra(CATEGORY_DESCRIPTION)
                )
            }
        }
    }

    companion object{
        val CATEGORY_DISPLAY_OBJECT = "CATEGORY_DISPLAY_OBJECT"
        val DELETE_TYPE_CATEGORY = "DELETE_CATEGORY"
        val DELETE_TYPE_TASK = "DELETE_TASK"

        val CATEGORY_KEY: String = "CATEGORY_KEY"
        val CATEGORY_TITLE: String = "CATEGORY_TITLE"
        val CATEGORY_DESCRIPTION: String = "CATEGORY_DESCRIPTION"
    }

}

internal class DeleteCategoryAndTaskDialogFragment(categoryId: String, taskId: String = "")
    : DialogFragment(){

    internal var deleteType: Any? = null

    private val user = FirebaseAuth.getInstance().currentUser
    private var categoryToDelete: String = categoryId
    private var taskToDelete: String = taskId
    private lateinit var database: DatabaseReference

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        database = FirebaseDatabase.getInstance().reference
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder: AlertDialog.Builder
            if (deleteType != null && deleteType!!.equals(CategoryDisplayActivity.DELETE_TYPE_CATEGORY))
                builder = createDeleteCategoryBuilder(it)
            else if (deleteType != null && deleteType!!.equals(CategoryDisplayActivity.DELETE_TYPE_TASK))
                builder = createDeleteTaskBuilder(it)
            else
                builder = createEmptyBuilder(it)
            return builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun createDeleteCategoryBuilder(it: FragmentActivity): AlertDialog.Builder {
        val builder = AlertDialog.Builder(it)
        builder
            .setTitle(R.string.delete_category)
            .setMessage(R.string.delete_category_message)
            .setPositiveButton("Delete"){_, _ ->
                database.child(user!!.uid).child(categoryToDelete).removeValue()
                Toast.makeText(context, "Category Deleted", Toast.LENGTH_SHORT).show()
                activity!!.finish()
            }
            .setNegativeButton("Cancel", null)
        return builder;
    }

    private fun createDeleteTaskBuilder(it: FragmentActivity): AlertDialog.Builder{
        val builder = AlertDialog.Builder(it)
        builder
            .setTitle(R.string.delete_task)
            .setMessage(R.string.delete_task_message)
            .setPositiveButton("Delete"){_, _ ->
                database.child(user!!.uid)
                    .child(categoryToDelete)
                    .child("tasklist")
                    .child(taskToDelete)
                    .removeValue()
                Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
        return builder;
    }

    private fun createEmptyBuilder(it: FragmentActivity): AlertDialog.Builder{
        val builder = AlertDialog.Builder(it)
        builder
            .setTitle(R.string.error_deleting)
            .setMessage(R.string.error_deleting_message)
            .setPositiveButton("Ok", null)
        return builder;
    }
}

internal class TaskReadAdapter : BaseAdapter(){

    var taskListItems = ArrayList<Task>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var localConvertView = convertView
        if (localConvertView == null){
            localConvertView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.activity_task_display, parent, false)
        }
        val task: Task = getItem(position) as Task
        setTaskText(task.title, localConvertView!!)
        setTaskDueDate(task.dueDate, localConvertView)
        return localConvertView
    }

    private fun setTaskText(title: String, localConvertView: View){
        val taskText = localConvertView.findViewById<TextView>(R.id.taskTitle)
        taskText.text = title
    }

    private fun setTaskDueDate(dueDate: String, localConvertView: View){
        val taskDueDate = localConvertView.findViewById<TextView>(R.id.taskDueDate)
        taskDueDate.text = dueDate;
    }

    override fun getItem(position: Int): Any {
        return taskListItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return taskListItems.size;
    }

}

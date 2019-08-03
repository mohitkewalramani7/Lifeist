package com.example.lifeist.category

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.*
import android.widget.*
import com.example.lifeist.HomeActivity
import com.example.lifeist.R
import com.example.lifeist.task.CreateAndEditTaskActivity
import com.example.lifeist.task.Task

class CategoryDisplayActivity : AppCompatActivity() {

    private lateinit var taskList: ListView;
    private lateinit var addTaskButton: FloatingActionButton;
    private lateinit var bottomSheetTaskViewer: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_display)
        initializeTaskListview()
        setAddTaskButtonClickListener()
        initializeBottomSheetTaskViewer()
    }

    private fun initializeTaskListview(){
        taskList = findViewById(R.id.taskListview)
        taskList.adapter = populateTasksList()
        setTaskListItemClickListener()
    }

    private fun populateTasksList(): TaskReadAdapter {
        val tasks = TaskReadAdapter()
        tasks.taskListItems = HomeActivity.taskList
        return tasks
    }

    private fun setTaskListItemClickListener(){
        taskList.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            if (bottomSheetTaskViewer.state == BottomSheetBehavior.STATE_HIDDEN)
                bottomSheetTaskViewer.state = BottomSheetBehavior.STATE_EXPANDED
            else
                bottomSheetTaskViewer.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun setAddTaskButtonClickListener(){
        addTaskButton = findViewById(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            showAddTaskActivity()
        }
    }

    private fun showAddTaskActivity(){
        val intent = Intent(this, CreateAndEditTaskActivity::class.java)
        intent.putExtra(CreateAndEditTaskActivity.INTENT_TYPE,
            CreateAndEditTaskActivity.INTENT_TYPE_CREATE)
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
            intent.putExtra(CreateAndEditTaskActivity.INTENT_TYPE, CreateAndEditTaskActivity.INTENT_TYPE_UPDATE)
            intent.putExtra(CreateAndEditTaskActivity.INTENT_UPDATE_TASK_OBJECT,
                "") // TODO
            startActivity(intent)
        }
    }

    private fun initializeDeleteTaskButton(deleteTaskButton: Button){
        deleteTaskButton.setOnClickListener{
            val deleteFragment = DeleteCategoryAndTaskDialogFragment()
            deleteFragment.deleteTarget = DELETE_TYPE_TASK
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
        val intent = Intent(this, CreateAndEditCategoryActivity::class.java)
        intent.putExtra(
            CreateAndEditCategoryActivity.INTENT_TYPE,
            CreateAndEditCategoryActivity.EDIT_CATEGORY
        )
        startActivity(intent)
        return true
    }

    private fun showDeleteCategoryConfirmationPopup(): Boolean{
        val deleteFragment = DeleteCategoryAndTaskDialogFragment()
        deleteFragment.deleteTarget = DELETE_TYPE_CATEGORY
        deleteFragment.show(supportFragmentManager, "delete_category")
        return true
    }

    companion object{
        val DELETE_TYPE_CATEGORY = "DELETE_CATEGORY"
        val DELETE_TYPE_TASK = "DELETE_TASK"
    }

}

internal class DeleteCategoryAndTaskDialogFragment: DialogFragment(){

    internal var deleteTarget: Any? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder: AlertDialog.Builder
            if (deleteTarget != null && deleteTarget!!.equals(CategoryDisplayActivity.DELETE_TYPE_CATEGORY))
                builder = createDeleteCategoryBuilder(it)
            else if (deleteTarget != null && deleteTarget!!.equals(CategoryDisplayActivity.DELETE_TYPE_TASK))
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
            .setPositiveButton("Delete", null) // TODO
            .setNegativeButton("Cancel", null) // TODO
        return builder;
    }

    private fun createDeleteTaskBuilder(it: FragmentActivity): AlertDialog.Builder{
        val builder = AlertDialog.Builder(it)
        builder
            .setTitle(R.string.delete_task)
            .setMessage(R.string.delete_task_message)
            .setPositiveButton("Delete", null) // TODO
            .setNegativeButton("Cancel", null) // TODO
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

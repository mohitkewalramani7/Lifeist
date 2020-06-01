package com.example.lifeist

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lifeist.category.Category
import com.example.lifeist.fragments.AllDatesFragment
import com.example.lifeist.fragments.DashboardFragment
import com.example.lifeist.fragments.PreferencesFragment
import com.example.lifeist.task.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.collections.ArrayList

class HomeActivity :
    AppCompatActivity(),
    AllDatesFragment.OnFragmentInteractionListener,
    DashboardFragment.OnFragmentInteractionListener,
    PreferencesFragment.OnFragmentInteractionListener {

    private lateinit var bottomView: BottomNavigationView

    private var allDates: AllDatesFragment? = null
    private var dashboard: DashboardFragment? = null
    private var preferences: PreferencesFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        categoryList = constructCategoryDummyData()
        dateList = constructDatesDummyData()
        taskList = constructTasksData()
        bottomView = findViewById(R.id.bottomNavigationView)
        setupBottomNavigationListener()
        bottomView.selectedItemId = R.id.dashboard
    }

    private fun constructCategoryDummyData() : ArrayList<Category> {
        val categoryList = arrayListOf<Category>()
        for (i in 0..15){
            val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz"
            val categoryTitle = (1..12).map { allowedChars.random() }.joinToString("")
            val categoryCount = (1..15).random()
            categoryList.add(Category("", categoryTitle, ""))
        }
        return categoryList
    }

    private fun constructDatesDummyData() : ArrayList<String> {
        val miniList = arrayListOf<String>()
        miniList.add("2019-08-01")
        miniList.add("2019-08-05")
        miniList.add("2019-08-07")
        miniList.add("2019-08-12")
        miniList.add("2019-08-25")
        return miniList
    }

    private fun constructTasksData() : ArrayList<Task> {
        val taskList = arrayListOf<Task>()
        taskList.add(Task("A", "First", "2019-7-28"))
        taskList.add(Task("B", "Second", "2019-7-29"))
        taskList.add(Task("C", "Third", "2019-7-30"))
        taskList.add(Task("D", "Fourth", "2019-7-21"))
        taskList.add(Task("E", "Fifth", "2019-7-15"))
        return taskList
    }

    private fun setupBottomNavigationListener(){
        bottomView.setOnNavigationItemSelectedListener {item ->
            bottomNavigationAction(item)
        }
    }

    private fun bottomNavigationAction(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.all_dates){
            if (allDates == null){
                allDates = AllDatesFragment.newInstance()
            }
            setupTransaction(allDates!!)
            return true
        }
        else if (menuItem.itemId == R.id.dashboard){
            if (dashboard == null){
                dashboard = DashboardFragment.newInstance()
            }
            setupTransaction(dashboard!!)
            return true
        }
        else if (menuItem.itemId == R.id.my_preferences){
            if (preferences == null){
                preferences = PreferencesFragment.newInstance()
            }
            setupTransaction(preferences!!)
            return true
        }
        return false
    }

    private fun setupTransaction(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainFragment, fragment)
        transaction.commit()
    }

    override fun onFragmentInteraction(uri: Uri) {
        Toast.makeText(applicationContext, "YAAA", Toast.LENGTH_SHORT).show()
    }

    companion object {
        var categoryList = ArrayList<Category>()
        var dateList = ArrayList<String>()
        var taskList = ArrayList<Task>()
    }

}

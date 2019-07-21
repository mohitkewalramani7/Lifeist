package com.example.lifeist

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity :
    AppCompatActivity(),
    AllDatesFragment.OnFragmentInteractionListener,
    DashboardFragment.OnFragmentInteractionListener,
    PreferencesFragment.OnFragmentInteractionListener {

    private var allDates: AllDatesFragment? = null
    private var dashboard: DashboardFragment? = null
    private var preferences: PreferencesFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        categoryList = constructCategoryDummyData()
        taskList = constructTasksDummyData()
        setupBottomNavigationListener()
        bottomNavigationView.selectedItemId = R.id.dashboard
    }

    private fun constructCategoryDummyData() : ArrayList<Category> {
        val categoryList = arrayListOf<Category>()
        for (i in 0..15){
            val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz"
            val categoryTitle = (1..12).map { allowedChars.random() }.joinToString("")
            val categoryCount = (1..15).random()
            categoryList.add(Category(categoryTitle, categoryCount))
        }
        return categoryList
    }

    private fun constructTasksDummyData() : ArrayList<String> {
        val miniList = arrayListOf<String>()
        miniList.add("2019-08-01")
        miniList.add("2019-08-05")
        miniList.add("2019-08-07")
        miniList.add("2019-08-12")
        miniList.add("2019-08-25")
        return miniList
    }

    private fun setupBottomNavigationListener(){
        val bottomView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
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
        var taskList = ArrayList<String>()
    }

}

package com.example.lifeist

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast

class HomeActivity : AppCompatActivity(), DashboardFragment.OnFragmentInteractionListener {

    private var dashboard: DashboardFragment? = null

    override fun onFragmentInteraction(uri: Uri) {
        Toast.makeText(applicationContext, "YAAA", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigationListener()
    }

    private fun setupBottomNavigationListener(){
        val bottomView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomView.setOnNavigationItemSelectedListener {item ->
            bottomNavigationAction(item)
        }
    }

    private fun bottomNavigationAction(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.all_dates){
            Toast.makeText(applicationContext, R.string.all_dates, Toast.LENGTH_SHORT).show()
            return true
        }
        else if (menuItem.itemId == R.id.dashboard){
            Toast.makeText(applicationContext, R.string.dashboard, Toast.LENGTH_SHORT).show()
            if (dashboard == null){
                dashboard = DashboardFragment.newInstance()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.mainFragment, dashboard!!)
                transaction.commit()
            }
            return true
        }
        else if (menuItem.itemId == R.id.my_preferences){
            Toast.makeText(applicationContext, R.string.my_preferences, Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

}

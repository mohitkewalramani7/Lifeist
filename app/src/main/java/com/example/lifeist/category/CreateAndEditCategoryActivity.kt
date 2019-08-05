package com.example.lifeist.category

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lifeist.R
import com.google.android.material.button.MaterialButton

class CreateAndEditCategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_and_edit_category)
        val intentType = intent.getStringExtra(INTENT_TYPE)
        renderRelevantLayout(intentType)
        cancelButtonSetup()
    }

    private fun renderRelevantLayout(intentType: String){
        if (intentType == CREATE_CATEGORY)
            renderCreateLayout()
        else
            renderEditLayout()
    }

    private fun renderCreateLayout(){
        // nothing
    }

    private fun renderEditLayout(){
        findViewById<TextView>(R.id.activityTitle).text = resources.getString(R.string.update_category)
        findViewById<MaterialButton>(R.id.createCategory).text = resources.getString(R.string.update_category)
        // TODO - display content to update
    }

    private fun cancelButtonSetup(){
        findViewById<MaterialButton>(R.id.cancelCategory).setOnClickListener {
            this.finish()
        }
    }

    companion object{
        val INTENT_TYPE: String = "TYPE"
        val CREATE_CATEGORY: String = "CREATE"
        val EDIT_CATEGORY: String = "EDIT"
    }

}

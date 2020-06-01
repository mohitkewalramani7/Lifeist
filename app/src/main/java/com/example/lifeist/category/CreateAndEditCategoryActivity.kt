package com.example.lifeist.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lifeist.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateAndEditCategoryActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_and_edit_category)
        val intentType = intent.getStringExtra(INTENT_TYPE)
        database = FirebaseDatabase.getInstance().reference;
        renderRelevantLayout(intentType)
        createCategory()
        cancelButtonSetup()
    }

    private fun renderRelevantLayout(intentType: String){
        // Nothing to do for Create Layout
        if (intentType == EDIT_CATEGORY)
            renderEditLayout()
    }

    private fun renderEditLayout(){
        findViewById<TextView>(R.id.activityTitle).text = resources.getString(R.string.update_category)
        findViewById<MaterialButton>(R.id.createCategory).text = resources.getString(R.string.update_category)

        findViewById<TextInputEditText>(R.id.categoryTitleInputText).setText(
            intent.getStringExtra(CategoryDisplayActivity.CATEGORY_TITLE))
        findViewById<TextInputEditText>(R.id.categoryDescriptionInputText).setText(
            intent.getStringExtra(CategoryDisplayActivity.CATEGORY_DESCRIPTION))
    }

    private fun createCategory(){
        val user = FirebaseAuth.getInstance().currentUser
        findViewById<Button>(R.id.createCategory).setOnClickListener{
            val enteredTitleResult = checkEnteredTitle()
            if (enteredTitleResult == false){
                return@setOnClickListener
            }

            val newCategoryDescription: String =
                findViewById<TextInputEditText>(R.id.categoryDescriptionInputText)
                    .text.toString()

            var key = intent.getStringExtra(CategoryDisplayActivity.CATEGORY_KEY)
            if (key == null){
                key = ""
            }

            val newCategory = Category(
                key,
                enteredTitleResult as String,
                newCategoryDescription
            )

            if (intent.getStringExtra(INTENT_TYPE) == CREATE_CATEGORY)
                performCreate(user!!, newCategory)
            else
                performUpdate(user!!, newCategory)
        }
    }

    private fun checkEnteredTitle(): Any {
        val newCategoryTitle=
            findViewById<TextInputEditText>(R.id.categoryTitleInputText).text.toString()
        if (newCategoryTitle == ""){
            Toast.makeText(applicationContext, "Please Enter a Category Name",
                Toast.LENGTH_SHORT).show()
            return false;
        }
        return newCategoryTitle
    }

    private fun performCreate(user: FirebaseUser, newCategory: Category){
        val newKey = database.child(user.uid).push()
        val newKeyVal = newKey.key
        database.child(user.uid).child(newKeyVal!!).setValue(newCategory)
            .addOnSuccessListener {
                Toast.makeText(applicationContext,
                    "Category Added", Toast.LENGTH_LONG).show()
                this.finish()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun performUpdate(user: FirebaseUser, newCategory: Category){
        val categoryUpdates: HashMap<String, Any> = HashMap()
        categoryUpdates.put(
            "/${user.uid}/${newCategory.key}/title",
            newCategory.title
        )
        categoryUpdates.put(
            "/${user.uid}/${newCategory.key}/description",
            newCategory.description
        )
        database.updateChildren(categoryUpdates).addOnSuccessListener {
            Toast.makeText(applicationContext,
                "Category Updated", Toast.LENGTH_SHORT).show()
            val resultIntent = Intent()
            resultIntent.putExtra(
                CategoryDisplayActivity.CATEGORY_TITLE,
                newCategory.title
            )
            resultIntent.putExtra(
                CategoryDisplayActivity.CATEGORY_DESCRIPTION,
                newCategory.description
            )
            setResult(0, resultIntent)
            this.finish()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
            }
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

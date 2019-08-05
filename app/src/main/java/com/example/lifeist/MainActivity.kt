package com.example.lifeist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var emailAddress: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailAddress = findViewById(R.id.emailInputText)
        password = findViewById(R.id.passwordInputText)
        auth = FirebaseAuth.getInstance()
        setupLoginListener()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser;
        if (currentUser != null){
            val intent = Intent(this, HomeActivity::class.java)
            // TODO - PUT USER ID IN
            startActivity(intent)
        }
    }

    private fun setupLoginListener(){
        val loginButton = findViewById<MaterialButton>(R.id.loginButton)
        loginButton.setOnClickListener{
            login()
        }
    }

    private fun login(){
        val emailText = emailAddress.text!!.toString()
        val passwordText = password.text!!.toString()

        if (!validateLoginFieldsEntry()){
            displayErrorMessage()
            return
        }

        auth.signInWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    displayErrorMessage()
                }
            }
    }

    private fun validateLoginFieldsEntry(): Boolean{
        if (emailAddress.text == null || emailAddress.text.toString().isNotEmpty()
            && password.text != null && password.text.toString().isNotEmpty())
            return true
        Log.w(TAG, "Null/empty email/password")
        return false
    }

    private fun displayErrorMessage(){
        Toast.makeText(baseContext, "Invalid username or password",
            Toast.LENGTH_SHORT).show()
    }

}

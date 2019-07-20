package com.example.lifeist

import android.content.Intent
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val USERNAME = "mohit"
    private val PASSWORD = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupLoginListener()
    }

    private fun setupLoginListener(){
        val loginButton = findViewById<MaterialButton>(R.id.loginButton)
        loginButton.setOnClickListener{
            login()
        }
    }

    private fun login(){
        val emailAddress = findViewById<TextInputEditText>(R.id.emailInputText)
        val password = findViewById<TextInputEditText>(R.id.passwordInputText)
        if (emailAddress.text!!.toString().equals(USERNAME) && password.text!!.toString().equals(PASSWORD)){
            Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(applicationContext, "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }
}

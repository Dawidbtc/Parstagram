package com.example.parstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(ParseUser.getCurrentUser() != null){
            goMain()
        }
        findViewById<Button>(R.id.loginButton).setOnClickListener{
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            login(username,password)
        }

        findViewById<Button>(R.id.signup).setOnClickListener{
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            signup(username,password)
        }
    }

    private fun signup(username: String, password: String){
        val user = ParseUser()
        user.setUsername(username)
        user.setPassword(password)
        user.signUpInBackground { e ->
            if (e == null){
                Toast.makeText(this,"Successfuly signed up",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Error Signing Up",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun login(username: String, password: String){
        ParseUser.logInInBackground(username,password, ({ user, e ->
            if (user != null){
                Log.i("Login:","Successfuly logged in")
                goMain()
            }else{
                Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()


            }})
        )
    }
    private fun goMain(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
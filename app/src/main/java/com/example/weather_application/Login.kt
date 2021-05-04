package com.example.weather_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_screen.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        login_loginbt.setOnClickListener {
            performLogin()
        }

        bt_back.setOnClickListener {
            finish()
        }
    }

    private fun performLogin() {
        val email = editTextPerson_Address.text.toString()
        val password = editTextPerson_Pass.text.toString()
        val feedIntent = Intent(this, Feed::class.java)
        // Close app on return
        feedIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill email/password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Main", "Attempt login with email/password: $email/***")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener


                    // else if successful
                    Log.d("Main","User logged in with uid: ${it.result?.user?.uid}")
                    startActivity(feedIntent)
                }
                .addOnFailureListener {
                    Log.d("Main", "Failed to login: ${it.message}")
                    Toast.makeText(this, "Failed to login: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

}
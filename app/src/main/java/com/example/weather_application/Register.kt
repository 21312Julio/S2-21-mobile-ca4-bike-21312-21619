package com.example.weather_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.register_screen.*

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        //Hiding Status Bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_screen)

        val registerBackButton = findViewById<Button>(R.id.bt_back2)

        registerBackButton.setOnClickListener {
            finish()
        }

        register_registerbt.setOnClickListener {
            performRegister()
        }
    }

    private fun performRegister() {
        val fname = editText_FirstNameRegister.text.toString()
        val sname = editText_SecondNameRegister.text.toString()
        val email = editText_EmailRegister.text.toString()
        val password = editText_PasswordRegister.text.toString()

        if (email.isEmpty() || password.isEmpty() || fname.isEmpty() || sname.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Register", "Email is: $email")
        Log.d("Register", "Password is: $password")

        //Firebase authentication to create an user using the data provided
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener


                    // else if successful
                    Log.d("Main","Successfully created user with uid: ${it.result?.user?.uid}")
                    saveUserToDatabase()
                }
                .addOnFailureListener {
                    Log.d("Main", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun saveUserToDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, editText_FirstNameRegister.text.toString(), editText_SecondNameRegister.text.toString())

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("Register", "User saved to firebase database")
                    val feedIntent = Intent(this, Feed::class.java)
                    // Close app on return
                    feedIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(feedIntent)
                }
                .addOnFailureListener {
                    Log.d("Register", "Failed to save user to database")
                }
    }

}

class User(val uid: String, val firstName: String, val secondName: String)
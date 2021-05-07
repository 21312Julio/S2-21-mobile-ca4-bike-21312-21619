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

        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_screen)

        val registerBackButton = findViewById<Button>(R.id.bt_back2)

        // Go back to Splash Page by finishing current page
        registerBackButton.setOnClickListener {
            finish()
        }

        // Perform register function and load feed
        register_registerbt.setOnClickListener {
            performRegister()
        }
    }


    private fun performRegister() {
        // Creating variables with values passed to the Edit Text Fields
        val fname = editText_FirstNameRegister.text.toString()
        val sname = editText_SecondNameRegister.text.toString()
        val email = editText_EmailRegister.text.toString()
        val password = editText_PasswordRegister.text.toString()

        // Checking if any fields are empty
        if (email.isEmpty() || password.isEmpty() || fname.isEmpty() || sname.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Register", "Email is: $email")
        Log.d("Register", "Password is: $password")

        //Firebase authentication to register user using email and password
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
        // Current logged instance and user ID
        val uid = FirebaseAuth.getInstance().uid ?: ""
        // Database reference for matching with current instance
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        // Create user object with given data FIRST NAME and SECOND NAME
        val user = User(uid, editText_FirstNameRegister.text.toString(), editText_SecondNameRegister.text.toString())

        ref.setValue(user)
                // Saving data onto database where UID matches the current instance one
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

// User class and attributes
class User(val uid: String, val firstName: String, val secondName: String)
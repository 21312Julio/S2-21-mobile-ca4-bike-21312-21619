package com.example.weather_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.profile_screen.*

class Profile : AppCompatActivity() {

    private lateinit var mDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_screen)

        bt_back3.setOnClickListener {
            finish()
        }

        profile_resetPassword.setOnClickListener {
            resetEmail()
        }

        retrieveData()


        FirebaseAuth.getInstance().currentUser.email
    }

    private fun retrieveData() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        var getData = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (uid != null) {
                    var firstName = snapshot.child("users").child(uid).child("firstName").value
                    var secondName = snapshot.child("users").child(uid).child("secondName").value
                    val users = User(uid, firstName as String, secondName as String)
                    editText_FirstNameEdit.setText(users.firstName)
                    editText_SecondNameEdit.setText(users.secondName)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        mDatabaseReference = FirebaseDatabase.getInstance().reference
        mDatabaseReference.addValueEventListener(getData)
        mDatabaseReference.addListenerForSingleValueEvent(getData)
    }

    private fun resetEmail() {
        val email = FirebaseAuth.getInstance().currentUser.email
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    // else if successful
                    Log.d("Reset","Password Reset Email Sent!")
                    Toast.makeText(this, "Password Reset Email Sent", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.d("Reset", "Failed to reset Password: ${it.message}")
                    Toast.makeText(this, "Failed to reset password: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

}
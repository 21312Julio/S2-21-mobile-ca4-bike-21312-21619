package com.example.weather_application

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val splashLoginButton = findViewById<Button>(R.id.splash_loginbt)
        val splashRegisterButton = findViewById<Button>(R.id.splash_registerbt)

        splashLoginButton.setOnClickListener {
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
        }

        splashRegisterButton.setOnClickListener {
            val registerIntent = Intent(this, Register::class.java)
            startActivity(registerIntent)
        }
    }
}



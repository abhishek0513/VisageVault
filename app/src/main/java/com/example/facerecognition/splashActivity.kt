package com.example.facerecognition

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class splashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val layout = findViewById<ConstraintLayout>(R.id.main2)
        layout.setBackgroundResource(R.drawable.background_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            // Start the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Finish SplashActivity to prevent going back to it
            finish()
        }, 8000)
    }
}
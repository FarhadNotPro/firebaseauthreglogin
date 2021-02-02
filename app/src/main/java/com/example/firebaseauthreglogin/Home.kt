package com.example.firebaseauthreglogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {
    private lateinit var btnLogOut : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnLogOut = findViewById(R.id.btnLogout)

        btnLogOut.setOnClickListener(){
            Firebase.auth.signOut()
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        }

    }

}
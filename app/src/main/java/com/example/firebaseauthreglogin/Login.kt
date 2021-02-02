package com.example.firebaseauthreglogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var aut: FirebaseAuth

    private lateinit var edEmail :EditText
    private lateinit var edPassword : EditText
    private lateinit var btnLogin : Button
    private lateinit var tvAlreadyHaveAnAccount : TextView
    private lateinit var email : String
    private lateinit var password : String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        aut = Firebase.auth

        edEmail = findViewById(R.id.edLEmail)
        edPassword = findViewById(R.id.edLPassword)
        btnLogin= findViewById(R.id.btnLogin)
        tvAlreadyHaveAnAccount = findViewById(R.id.tvAlreadyHaveAnAccount)

        btnLogin.setOnClickListener(){ Login() }

        tvAlreadyHaveAnAccount.setOnClickListener(){
            finish()
        }
    }



    private fun Login(){
        email = edEmail.text.toString()
        password = edPassword.text.toString()
        Log.d("Main", email)
        Log.d("Main", password)
        aut.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Main", "signInWithEmail:success")
                    val intent = Intent(this,Home::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Main", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }

            }
    }
}
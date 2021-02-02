package com.example.firebaseauthreglogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.firebaseauthreglogin.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*

class Register : AppCompatActivity() {

    private lateinit var edName : EditText
    private lateinit var edEmail :EditText
    private lateinit var edPassword :EditText
    private lateinit var btnRegister :Button
    private lateinit var ivImageSelector:ImageView
    private lateinit var tvAlreadyHaveAnAccount :TextView

    private lateinit var name : String
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var aut: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edName = findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        edPassword = findViewById(R.id.edPassword)
        btnRegister= findViewById(R.id.btnRegister)
        ivImageSelector= findViewById(R.id.ivImageSelect)
        tvAlreadyHaveAnAccount = findViewById(R.id.tvAlreadyHaveAnAccount)
        aut = Firebase.auth

        ivImageSelector.setOnClickListener(){

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)

        }

        btnRegister.setOnClickListener{ performRegisteration() }

        tvAlreadyHaveAnAccount.setOnClickListener(){
           val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }


    }

    var selectedPhotoUri : Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("Register", "Photo Was Selected")
            selectedPhotoUri = data.data

            Picasso.get()
                .load(selectedPhotoUri)
                .resize(50, 50)
                .centerCrop()
                .into(ivImageSelector)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = aut.currentUser
        if(currentUser != null){
            val intent = Intent(this,Home::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun performRegisteration(){
        name = edName.text.toString()
        email = edEmail.text.toString()
        password = edPassword.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all Credentials", Toast.LENGTH_SHORT).show()
            return
        }

        aut.createUserWithEmailAndPassword(edEmail.text.toString(), edPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    //  Log.d("Main", edEmail.text.toString())
                    //  Log.d("Main", edPassword.text.toString())

                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this,"createUserWithEmail:success"+task.exception,Toast.LENGTH_SHORT).show()
                        uploadImageToFirebaseStorage()
                        Log.d("Register", "createUserWithEmail:success")
                        val intent = Intent(this,Login::class.java)
                        startActivity(intent)

                    } else {
                        // If Registration fails, display a message to the user.
                        Toast.makeText(this,""+task.exception,Toast.LENGTH_SHORT).show()
                        Log.w("Register", "createUserWithEmail:failure", task.exception)
                    }


                }
    }

    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return

        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Register", "Photo uploaded Successfully: ${it.metadata?.path}")
                saveUserDataToFirebaseDatabase(it.toString())
            }
            .addOnFailureListener(){
                Log.d("Register", "Photo uploading Failed: ${it}")
            }
    }

    private fun saveUserDataToFirebaseDatabase(imageUrl:String){
        val uid = FirebaseAuth.getInstance().uid.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid")

        val user = User(uid,name,email,imageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register", "User data saved on Firebase Database")
            }
    }

}













/*            val bitmap : Bitmap

            if (android.os.Build.VERSION.SDK_INT >= 29){
                // To handle deprication use
                val source = ImageDecoder.createSource(contentResolver, selectedPhotoUri!!)
                bitmap = ImageDecoder.decodeBitmap(source)
                Log.d("Register", "Greater Than 29")
            } else{
                // Use older version
                Log.d("Register", "Lower Than 29")
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            }

            val bitmapDrawable = BitmapDrawable(bitmap)
            ivImageSelector.setBackgroundDrawable(bitmapDrawable)*/

/*  FirebaseAuth.getInstance().createUserWithEmailAndPassword(edEmail.text.toString(),edPassword.text.toString())
      .addOnCompleteListener {
          Log.d("Main","Not S")
          if(!it.isSuccessful){
              Log.d("Main","Not S")

              return@addOnCompleteListener
          }
          // else if successful
          Log.d("Main","User Created Successfully with user id: ${it.result?.user?.uid}")
      }
*/


//   Toast.makeText(this,email.toString(),Toast.LENGTH_SHORT).show()

//  Toast.makeText(this,email,Toast.LENGTH_SHORT).show()
//   Toast.makeText(this,password,Toast.LENGTH_SHORT).show()
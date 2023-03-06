package com.shubhamjain.justpics

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var email: String;
    private lateinit var password: String;
    private lateinit var emailBox: EditText;
    private lateinit var passwordBox: EditText;
    private lateinit var signInUserButton: Button;
    private lateinit var registerButton: TextView;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        auth = Firebase.auth;

        emailBox = findViewById<EditText>(R.id.emailBoxSignIn);
        passwordBox = findViewById<EditText>(R.id.passwordBoxSignIn);
        signInUserButton = findViewById<Button>(R.id.signInUserButton);
        registerButton = findViewById<TextView>(R.id.registerButton);

        signInUserButton.setOnClickListener {


            email = emailBox.text.toString();
            password = passwordBox.text.toString();

            if (email == "" || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailBox.setError("Can't be empty or Invalid!")
                return@setOnClickListener;
            }
            if (password == "" || password.length < 8) {
                passwordBox.setError("Can't be empty! or have less than 8 characters")
                return@setOnClickListener;
            }



            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser;

                    var userObj = UserClass();

                    val intent = Intent(this, Home::class.java);
                    startActivity(intent)

                } else {
                    Toast.makeText(applicationContext,task.exception?.message.toString(),Toast.LENGTH_LONG).show();
                }
            }

        }

        registerButton.setOnClickListener {
            val intent = Intent(this, ProfileSetUp::class.java);
            startActivity(intent)
        }


    }
}
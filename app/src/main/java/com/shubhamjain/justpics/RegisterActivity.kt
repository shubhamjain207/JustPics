package com.shubhamjain.justpics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var email: String;
    private lateinit var password: String;
    private lateinit var emailBox: EditText;
    private lateinit var passwordBox: EditText;
    private lateinit var registerUserButton: Button;
    private lateinit var signInButton: TextView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_activity)

        FirebaseApp.initializeApp(applicationContext);

        auth = Firebase.auth;

        if (auth.currentUser != null) {
            val intent = Intent(this, Home::class.java);
            startActivity(intent)
        }


        emailBox = findViewById<EditText>(R.id.emailBoxSignIn);
        passwordBox = findViewById<EditText>(R.id.passwordBoxSignIn);
        registerUserButton = findViewById<Button>(R.id.signInUserButton);
        signInButton = findViewById<TextView>(R.id.registerButton);



        registerUserButton.setOnClickListener {


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



            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser;


                    val intent = Intent(this, ProfileSetUp::class.java);


                    intent.putExtra("Email", email);
                    intent.putExtra("Password", password);
                    intent.putExtra("UserUid", user?.uid);
                    startActivity(intent)

                } else {
                            emailBox.setError(task.exception?.message.toString());
                }
            }

        }

        signInButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java);
            startActivity(intent)
        }


    }
}
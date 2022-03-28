package com.mik.recyloc

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity() {
    private var emailTextView: EditText? = null
    private var passwordTextView: EditText? = null
    private lateinit var Btn : Button
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance()

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email)
        passwordTextView = findViewById(R.id.passwd)
        Btn = findViewById(R.id.btnregister)

        // Set on Click Listener on Registration button
        Btn.setOnClickListener{
            registerNewUser()
        }

        redirectToLoginScreen()
    }

    private fun registerNewUser() {


        // Take the value of two edit texts in Strings
        val email: String
        val password: String
        email = emailTextView!!.text.toString()
        password = passwordTextView!!.text.toString()

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(
                applicationContext,
                "Please enter email!!",
                Toast.LENGTH_LONG
            )
                .show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(
                applicationContext,
                "Please enter password!!",
                Toast.LENGTH_LONG
            )
                .show()
            return
        }

        // create new user or register new user
        mAuth
            ?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Registration successful!",
                        Toast.LENGTH_LONG
                    )
                        .show()


                    // if the user created intent to login activity
                    val intent = Intent(
                        this@RegistrationActivity,
                        LoginActivity::class.java
                    )
                    startActivity(intent)
                } else {

                    // Registration failed
                    Toast.makeText(
                        applicationContext, "Registration failed!!"
                                + " Please try again later",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

    }

    private fun redirectToLoginScreen() {
        textViewLogin.setOnClickListener {
            val intent = Intent(this@RegistrationActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
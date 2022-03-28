package com.mik.recyloc

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.textViewLogin


class ResetPasswordActivity : AppCompatActivity() {

    private var emailTextView : EditText? = null
    private lateinit var Btn : Button
    private var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        mAuth = FirebaseAuth.getInstance()

        emailTextView = findViewById(R.id.email)
        Btn = findViewById(R.id.btnreset)

        Btn.setOnClickListener {
            sendPasswordResetEmail()
        }
        redirectToLoginScreen()
    }

    private fun sendPasswordResetEmail() {
        val emailAddress = emailTextView?.text.toString()
        Firebase.auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext,"Email sent!",Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(applicationContext,"Something went wrong!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun redirectToLoginScreen() {
        textViewLogin.setOnClickListener {
            val intent = Intent(this@ResetPasswordActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
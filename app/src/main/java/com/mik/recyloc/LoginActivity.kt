package com.mik.recyloc

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private var emailTextView: EditText? = null
    private var passwordTextView: EditText? = null
    private lateinit var Btn : Button
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email)
        passwordTextView = findViewById(R.id.password)
        Btn = findViewById(R.id.login)

        // Set on Click Listener on Sign-in button
        Btn.setOnClickListener{
            loginUserAccount()
        }


        redirectToRegistrationScreen()
        redirectToResetPasswordScreen()
        fetchCredentials()

    }

    private fun loginUserAccount() {

        // Take the value of two edit texts in Strings
        val email: String
        val password: String
        email = emailTextView!!.text.toString()
        password = passwordTextView!!.text.toString()

        // validations for input email and password
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

        // signin existing user
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Login successful!!",
                        Toast.LENGTH_LONG
                    )
                        .show()

                    // if sign-in is successful
                    // intent to home activity
                    if (checkboxKeepMeLoggedIn.isChecked) {
                        saveCredentials()
                    }else{
                        removeCredentials()
                    }
                    val intent = Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                } else {

                    // sign-in failed
                    Toast.makeText(
                        applicationContext,
                        "Login failed!!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
    }

    private fun redirectToRegistrationScreen() {
        textViewLogin.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun redirectToResetPasswordScreen() {
        textViewReset.setOnClickListener {
            val intent = Intent(this@LoginActivity,ResetPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getSecretSharedPref(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(context,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }


    private fun saveCredentials(){
        val editor = getSecretSharedPref(applicationContext).edit()
        editor.putString("name",emailTextView?.text.toString())
        editor.putString("password",passwordTextView?.text.toString())
        editor.putBoolean("ischecked",checkboxKeepMeLoggedIn.isChecked)
        editor.apply()
    }


    private fun fetchCredentials() {
        val sh = getSecretSharedPref(applicationContext)
        val email = sh.getString("name","")
        val password = sh.getString("password","")
        if (email != "" && password != "") {
            val ischecked = sh.getBoolean("ischeked",true)
            checkboxKeepMeLoggedIn.isChecked = ischecked
        }else {
            val ischecked = sh.getBoolean("ischeked",false)
            checkboxKeepMeLoggedIn.isChecked = ischecked
        }
        emailTextView?.setText(email)
        passwordTextView?.setText(password)

    }

    private fun removeCredentials() {
        val editor = getSecretSharedPref(applicationContext).edit()
        editor.remove("name")
        editor.remove("password")
        editor.remove("ischecked")
        editor.apply()

        checkboxKeepMeLoggedIn.isChecked = false
    }

}
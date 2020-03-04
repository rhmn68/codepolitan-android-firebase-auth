package com.rahmanaulia.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initActionBar()

        btnSignUp.setOnClickListener {
            val email = etEmailSignUp.text.toString().trim()
            val pass = etPasswordSignUp.text.toString().trim()
            val confirmPass = etConfirmPasswordSignUp.text.toString().trim()

            CustomDialog.showLoading(this)
            if (checkValidation(email, pass, confirmPass)){
                registerToServer(email, pass)
            }
        }

        tbSignUp.setNavigationOnClickListener {
            finish()
        }
    }

    private fun registerToServer(email: String, pass: String) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener{task ->
                CustomDialog.hideLoading()
                if (task.isSuccessful){
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
            }
            .addOnFailureListener{
                CustomDialog.hideLoading()
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkValidation(email: String, pass: String, confirmPass: String): Boolean {
        if (email.isEmpty()){
            etEmailSignUp.error = "Please field your email"
            etEmailSignUp.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmailSignUp.error = "Please use valida email"
            etEmailSignUp.requestFocus()
        }else if (pass.isEmpty()){
            etPasswordSignUp.error = "Please field your password"
            etPasswordSignUp.requestFocus()
        }else if (confirmPass.isEmpty()){
            etConfirmPasswordSignUp.error = "Please field your confirm password"
            etConfirmPasswordSignUp.requestFocus()
        }else if (pass != confirmPass){
            etPasswordSignUp.error = "Your pass didnt match"
            etConfirmPasswordSignUp.error = "Your confirm pass didnt match"

            etPasswordSignUp.requestFocus()
            etConfirmPasswordSignUp.requestFocus()
        }else{
            etPasswordSignUp.error = null
            etConfirmPasswordSignUp.error = null
            return true
        }
        CustomDialog.hideLoading()
        return false
    }

    private fun initActionBar() {
        setSupportActionBar(tbSignUp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }
}

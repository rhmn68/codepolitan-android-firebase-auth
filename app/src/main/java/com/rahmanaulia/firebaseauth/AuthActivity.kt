package com.rahmanaulia.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        btnSignInAuth.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        btnSignUpAuth.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}

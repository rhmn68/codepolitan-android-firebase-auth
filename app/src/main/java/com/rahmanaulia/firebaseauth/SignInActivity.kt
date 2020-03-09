package com.rahmanaulia.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlin.math.sign

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    companion object{
        const val RC_SIGN_IN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initActionBar()
        initFirebaseAuth()

        btnSignIn.setOnClickListener {
            val email = etEmailSignIn.text.toString().trim()
            val pass = etPasswordSignIn.text.toString().trim()

            if (checkValidation(email, pass)){
                loginToServer(email, pass)
            }
        }

        btnForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        tbSignIn.setNavigationOnClickListener {
            finish()
        }

        btnGoogleSignIn.setOnClickListener {
            val signIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            CustomDialog.showLoading(this)
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                fireBaseAuth(credential)
            }catch (e: ApiException){
                CustomDialog.hideLoading()
                Toast.makeText(this, "Sign-In Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginToServer(email: String, pass: String) {
        val credential = EmailAuthProvider.getCredential(email, pass)
        fireBaseAuth(credential)
    }

    private fun fireBaseAuth(credential: AuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                CustomDialog.hideLoading()
                if (task.isSuccessful){
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }else{
                    Toast.makeText(this, "Sign-In failed", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {exception ->
                CustomDialog.hideLoading()
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun initFirebaseAuth() {
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun checkValidation(email: String, pass: String): Boolean {
        if (email.isEmpty()){
            etEmailSignIn.error = "Please field your email"
            etEmailSignIn.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmailSignIn.error = "Please use valid email"
            etEmailSignIn.requestFocus()
        }else if (pass.isEmpty()){
            etPasswordSignIn.error = "Please field your password"
            etPasswordSignIn.requestFocus()
        }else{
            return true
        }
        CustomDialog.hideLoading()
        return false
    }

    private fun initActionBar() {
        setSupportActionBar(tbSignIn)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }
}

package com.coxtunes.learnfirebase.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coxtunes.learnfirebase.Activity.LoginActivity
import com.coxtunes.learnfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var emailedittext: EditText
    private lateinit var passwordedittext: EditText
    private lateinit var signInbtn: Button
    private var auth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailedittext = findViewById(R.id.email_login)
        passwordedittext = findViewById(R.id.password_login)
        signInbtn = findViewById(R.id.button_signin)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        signInbtn.setOnClickListener {
            val email = emailedittext.text.toString()
            val password = passwordedittext.text.toString()
            auth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Sign In Successfull", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@LoginActivity, SendNotificationActivity::class.java))
                }
            }.addOnFailureListener { e -> Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show() }
        }
    }

    fun gotoregistration(view: View?) {
        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        val firebaseUser = auth!!.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this@LoginActivity, SendNotificationActivity::class.java))
        }
    }
}
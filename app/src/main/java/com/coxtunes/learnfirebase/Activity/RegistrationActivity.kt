package com.coxtunes.learnfirebase.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coxtunes.learnfirebase.Activity.RegistrationActivity
import com.coxtunes.learnfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import java.util.*

class RegistrationActivity : AppCompatActivity() {
    private lateinit var emailedittext: EditText
    private lateinit var passwordedittext: EditText
    private lateinit var signupbtn: Button
    private var auth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null
    private var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        emailedittext = findViewById(R.id.email_login)
        passwordedittext = findViewById(R.id.password_login)
        signupbtn = findViewById(R.id.button_signin)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        /*
        * Generate Firebase Device Token for install app device
        * Get Firebase FCM token
        * */FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
            token = instanceIdResult.token
            Log.d("MyDeviceToken", token)
        }

        /*
        * Registration with Email & password for firebase email password authentication
        * */signupbtn.setOnClickListener(View.OnClickListener {
            val email = emailedittext.getText().toString()
            val password = passwordedittext.getText().toString()
            auth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                val uID = FirebaseAuth.getInstance().currentUser!!.uid
                uID?.let { sendDataToDatabase(it, email) }
            }.addOnFailureListener { e -> Toast.makeText(this@RegistrationActivity, e.message, Toast.LENGTH_LONG).show() }
        })
    }

    /*
    *  Send User Data To Firebase Database
    *
    * */
    private fun sendDataToDatabase(authID: String, email: String) {
        databaseReference!!.child("users").child(authID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // Check Same Data Exist in firebase Realtime Database if exist ignore to store same data in dB
                if (dataSnapshot.exists()) {
                    Log.d("firebaseuserdata", "User already exist")
                } else {
                    // Send Data to firebase Realtime Database
                    val users = HashMap<Any, Any?>()
                    users["Email"] = email
                    users["token"] = token
                    databaseReference!!.child("users").child(authID).setValue(users).addOnSuccessListener { Toast.makeText(applicationContext, "Sign up Successfully, Now You Can Sign In", Toast.LENGTH_LONG).show() }.addOnFailureListener { e -> Toast.makeText(this@RegistrationActivity, e.message, Toast.LENGTH_LONG).show() }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@RegistrationActivity, databaseError.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    // Go to login page999999999
    fun gotologin(view: View?) {
        startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
    }
}
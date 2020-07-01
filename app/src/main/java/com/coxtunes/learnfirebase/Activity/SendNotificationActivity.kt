package com.coxtunes.learnfirebase.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coxtunes.learnfirebase.Activity.SendNotificationActivity
import com.coxtunes.learnfirebase.NotificationHelper.*
import com.coxtunes.learnfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendNotificationActivity : AppCompatActivity() {
    private lateinit var notifysingleuserToken: EditText
    private lateinit var notificationTitle: EditText
    private lateinit var notificationMessage: EditText
    private lateinit var sendNotificationButton: Button
    private lateinit var  apiService: NotificationAPIService
    private var gentoken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_notification)
        apiService = RetrofitClient.getClient("https://fcm.googleapis.com/")!!.create(NotificationAPIService::class.java)
        notificationTitle = findViewById(R.id.notication_title)
        notificationMessage = findViewById(R.id.notification_message)
        notifysingleuserToken = findViewById(R.id.notification_userid)
        sendNotificationButton = findViewById(R.id.send_notification_button)

        val a = intent
        if (a != null) {
            val title = a.getStringExtra("Title")
            val message = a.getStringExtra("Message")
            Toast.makeText(this, title + message, Toast.LENGTH_LONG).show()
        }
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
            gentoken = instanceIdResult.token
            Log.d("MyDeviceToken", gentoken)
        }


        /*
        * Send Notification To Specific Token Device
        * */sendNotificationButton.setOnClickListener(View.OnClickListener {
            // QUERY FOR user input token From all user node
            FirebaseDatabase.getInstance().reference.child("users").child(notifysingleuserToken.getText().toString().trim { it <= ' ' }).child("token").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // If match then get token
                    val usertoken = dataSnapshot.getValue(String::class.java)!!
                    // Send Notification to this token finally yehhhh :) (Calling Method)
                    sendNotificationsfunctionality(usertoken, notificationTitle.getText().toString().trim(), notificationMessage.getText().toString().trim())
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@SendNotificationActivity, databaseError.message, Toast.LENGTH_SHORT).show()
                }
            })
        })

        /*
        *  Get Alltime Update Token
        * */
        //updateTokenToDatabaseNode();
    }

    /*
    *  Send Notification
    *
    * */
    fun sendNotificationsfunctionality(usertoken: String?, title: String?, message: String?) {
        val data = NotificationDataModel(title, message)
        val sender = NotificationSenderModel(data, usertoken)
        val call = apiService.sendNotification(sender)
        call.enqueue(object : Callback<MyResponseModel>{
            override fun onFailure(call: Call<MyResponseModel>, t: Throwable) {
                Toast.makeText(this@SendNotificationActivity, t.message, Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<MyResponseModel>, response: Response<MyResponseModel>) {
                if (response.code() == 200) {
                    if (response.body()!!.success != 1) {
                        Toast.makeText(this@SendNotificationActivity, "Failed ", Toast.LENGTH_LONG)
                    } else {
                        Toast.makeText(this@SendNotificationActivity, "Notification Send to Token User ", Toast.LENGTH_LONG)
                    }
                }
            }
        })
    }

    /*
    * Check token update or not if not send to db
    *
    * */
    //    private void updateTokenToDatabaseNode() {
    //        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    //        String refreshToken= FirebaseInstanceId.getInstance().getToken();
    //        TokenModel token1= new TokenModel(refreshToken);
    //        FirebaseDatabase.getInstance().getReference("users")
    //                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
    //                .child("Token").setValue(token1);
    //    }
    fun logoutnow(view: View?) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this@SendNotificationActivity, LoginActivity::class.java))
    }
}
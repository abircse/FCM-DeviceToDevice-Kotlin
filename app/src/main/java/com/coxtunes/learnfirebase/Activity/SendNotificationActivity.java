package com.coxtunes.learnfirebase.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coxtunes.learnfirebase.NotificationHelper.MyResponseModel;
import com.coxtunes.learnfirebase.NotificationHelper.NotificationAPIService;
import com.coxtunes.learnfirebase.NotificationHelper.NotificationDataModel;
import com.coxtunes.learnfirebase.NotificationHelper.NotificationSenderModel;
import com.coxtunes.learnfirebase.NotificationHelper.RetrofitClient;
import com.coxtunes.learnfirebase.NotificationHelper.TokenModel;
import com.coxtunes.learnfirebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotificationActivity extends AppCompatActivity {


    private EditText notifysingleuserToken;
    private EditText notificationTitle,notificationMessage;
    private Button sendNotificationButton;
    private NotificationAPIService apiService;
    private String gentoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        apiService = RetrofitClient.getClient("https://fcm.googleapis.com/").create(NotificationAPIService.class);
        notificationTitle = findViewById(R.id.notication_title);
        notificationMessage = findViewById(R.id.notification_message);
        notifysingleuserToken = findViewById(R.id.notification_userid);
        sendNotificationButton = findViewById(R.id.send_notification_button);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                gentoken = instanceIdResult.getToken();
                Log.d("MyDeviceToken", gentoken);
            }
        });


        /*
        * Send Notification To Specific Token Device
        * */
        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // QUERY FOR user input token From all user node
                FirebaseDatabase.getInstance().getReference().child("users").child(notifysingleuserToken.getText().toString().trim()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // If match then get token
                        String usertoken = dataSnapshot.getValue(String.class);
                        // Send Notification to this token finally yehhhh :) (Calling Method)
                        sendNotificationsfunctionality(usertoken,notificationTitle.getText().toString().trim(),notificationMessage.getText().toString().trim());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SendNotificationActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        /*
        *  Get Alltime Update Token
        * */
        //updateTokenToDatabaseNode();

    }

    /*
    *  Send Notification
    *
    * */

    public void sendNotificationsfunctionality(String usertoken, String title, String message)
    {
        NotificationDataModel data = new NotificationDataModel(title, message);
        NotificationSenderModel sender = new NotificationSenderModel(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponseModel>() {
            @Override
            public void onResponse(Call<MyResponseModel> call, Response<MyResponseModel> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(SendNotificationActivity.this, "Failed ", Toast.LENGTH_LONG);
                    }
                    else
                    {
                        Toast.makeText(SendNotificationActivity.this, "Notification Send to Token User ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponseModel> call, Throwable t) {
                Toast.makeText(SendNotificationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    public void logoutnow(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(SendNotificationActivity.this, LoginActivity.class));
    }
}

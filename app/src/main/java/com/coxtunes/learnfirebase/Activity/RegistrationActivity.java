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

import com.coxtunes.learnfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailedittext, passwordedittext;
    private Button signupbtn;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        emailedittext = findViewById(R.id.email_login);
        passwordedittext = findViewById(R.id.password_login);
        signupbtn = findViewById(R.id.button_signin);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        /*
        * Generate Firebase Device Token for install app device
        * Get Firebase FCM token
        * */
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
                Log.d("MyDeviceToken", token);
            }
        });

        /*
        * Registration with Email & password for firebase email password authentication
        * */
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String email = emailedittext.getText().toString();
                final String password = passwordedittext.getText().toString();

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if (uID!=null)
                        {
                            sendDataToDatabase(uID,email);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


    }

    /*
    *  Send User Data To Firebase Database
    *
    * */

    private void sendDataToDatabase(final String authID, final String email)
    {
        databaseReference.child("users").child(authID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Check Same Data Exist in firebase Realtime Database if exist ignore to store same data in dB
                if (dataSnapshot.exists())
                {
                    Log.d("firebaseuserdata", "User already exist");
                }
                else
                {
                    // Send Data to firebase Realtime Database
                    HashMap<Object, Object> users = new HashMap<>();
                    users.put("Email",email);
                    users.put("token",token);
                    databaseReference.child("users").child(authID).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            Toast.makeText(getApplicationContext(), "Sign up Successfully, Now You Can Sign In", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegistrationActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    // Go to login page999999999
    public void gotologin(View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }
}

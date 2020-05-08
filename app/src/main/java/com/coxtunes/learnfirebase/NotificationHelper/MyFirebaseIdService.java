package com.coxtunes.learnfirebase.NotificationHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        // Refresh Token
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        if(firebaseUser!=null){
            updateTokenToDatabaseNode(refreshToken);
        }
    }

    // AlTIME UPDATE CURRENT USER TOKEN NODE IN DATABASE (IF ACCIDENTLY CHANGE DEVICE TOKEN)
    private void updateTokenToDatabaseNode(String refreshToken) {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        TokenModel token1= new TokenModel(refreshToken);
        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("token").setValue(token1);
    }


}

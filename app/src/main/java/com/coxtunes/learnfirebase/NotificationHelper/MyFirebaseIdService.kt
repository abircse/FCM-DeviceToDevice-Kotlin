package com.coxtunes.learnfirebase.NotificationHelper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseIdService : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        // Refresh Token
        val refreshToken = FirebaseInstanceId.getInstance().token
        if (firebaseUser != null) {
            updateTokenToDatabaseNode(refreshToken)
        }
    }

    // AlTIME UPDATE CURRENT USER TOKEN NODE IN DATABASE (IF ACCIDENTLY CHANGE DEVICE TOKEN)
    private fun updateTokenToDatabaseNode(refreshToken: String?) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val token1 = TokenModel(refreshToken)
        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("token").setValue(token1)
    }
}
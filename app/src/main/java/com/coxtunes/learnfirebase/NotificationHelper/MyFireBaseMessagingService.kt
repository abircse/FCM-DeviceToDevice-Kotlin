package com.coxtunes.learnfirebase.NotificationHelper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.coxtunes.learnfirebase.Activity.SendNotificationActivity
import com.coxtunes.learnfirebase.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFireBaseMessagingService : FirebaseMessagingService() {
    var title: String? = null
    var message: String? = null
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        title = remoteMessage.data["title"]
        message = remoteMessage.data["message"]
        showNotificationMessage(title, message)
    }

    private fun showNotificationMessage(title: String?, message: String?) {
        var mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Send Notification Data to Activity
            val intent = Intent(this, SendNotificationActivity::class.java)
            intent.putExtra("Title", title)
            intent.putExtra("Message", message)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val notifyID = 1
            val CHANNEL_ID = "learnfirebase"
            val notification = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setChannelId(CHANNEL_ID).build()
            val notificationChannel = NotificationChannel(CHANNEL_ID, "com.coxtunes.learnfirebase", NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(notificationChannel)
            mNotificationManager.notify(notifyID, notification)
        } else {
            val intent = Intent(this, SendNotificationActivity::class.java)
            intent.putExtra("Title", title)
            intent.putExtra("Message", message)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val builder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setContentTitle(title)
                    .setContentIntent(pendingIntent)
                    .setContentText(message)
            mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(0, builder.build())
        }
    }
}
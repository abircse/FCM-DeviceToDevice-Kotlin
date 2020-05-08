package com.coxtunes.learnfirebase.NotificationHelper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.coxtunes.learnfirebase.Activity.SendNotificationActivity;
import com.coxtunes.learnfirebase.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    String title, message;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title = remoteMessage.getData().get("Title");
        message = remoteMessage.getData().get("Message");
        showNotificationMessage(title, message);
    }

    private void showNotificationMessage(String title, String message) {

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int notifyID = 1;
            String CHANNEL_ID = "my_channel_01";
            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setChannelId(CHANNEL_ID).build();

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "com.cbiu.internship", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(notificationChannel);
            mNotificationManager.notify(notifyID, notification);

        } else
        {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                    .setContentTitle(title)
                    .setContentText(message);
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, builder.build());
        }


    }
}

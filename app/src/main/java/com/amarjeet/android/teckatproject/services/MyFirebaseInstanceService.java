package com.amarjeet.android.teckatproject.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.amarjeet.android.teckatproject.R;
import com.google.firebase.BuildConfig;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFirebaseInstanceService extends FirebaseMessagingService {
    final long[] VIBRATE_PATTERN = {0, 500};
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
    public void showNotification(String title, String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"MyNotification")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentText(message)
                .setVibrate(VIBRATE_PATTERN);
        NotificationManagerCompat managerCompat= NotificationManagerCompat.from(this);
        managerCompat.notify(999,builder.build());
    }
}

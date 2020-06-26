package com.example.coffeetracker2;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class AlertBroadcast extends BroadcastReceiver {

    // String for AndroidManifest
    private static final String NOTIFICATION_BROADCAST_ALERT = "RATINGNOTIF";
    public static final String FROM_NOTIFICATION = "com.example.coffeetracker2.FROM_NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        String CHANNEL_ID = "activity_channel";

        // Changed intent class to redirect to MainActivity when clicked and putExtra for knowing which fragment to open
        intent.setClass(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(FROM_NOTIFICATION, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the actual notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_coffee)
                .setContentTitle("Productivity level")
                .setContentText("Please rate your productivity level after the last coffee")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is must be a unique int for each notification
        Random random = new Random();
        int notificationId = random.nextInt(9999 - 1000) + 1000;

        notificationManager.notify(notificationId, builder.build());
    }
}

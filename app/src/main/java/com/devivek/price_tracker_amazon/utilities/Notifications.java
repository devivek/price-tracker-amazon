package com.devivek.price_tracker_amazon.utilities;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;


class Notifications {


    private Context context;


    Notifications(Context context) {
        this.context = context;
    }


    void sendNotification(String itemName){
        String CHANNEL_ID = "Notifications";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Price Tracker")
                .setContentText(itemName+" Price changed")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(itemName+" Price changed"))
                .setPriority(NotificationCompat.PRIORITY_MAX);

        Notification notification = mBuilder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notification);
    }

}

package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder notiBulider = notificationHelper.getChannelNotification(intent.getExtras().getString("name"), intent.getExtras().getString("description"));
        notificationHelper.getManager().notify(1, notiBulider.build());
    }
}

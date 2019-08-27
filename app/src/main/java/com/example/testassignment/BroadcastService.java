package com.example.testassignment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class BroadcastService extends Service {
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    CountDownTimer cdt = null;


    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager =
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        /*** Set Notification  ****/
        setNotification();

    }

    private void setNotification() {
//First time
        builder = new NotificationCompat.Builder(this);
        Notification notification = builder.setContentText(TimerFormat.getTimeString(300000))
                .setContentTitle("You Are on Break")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true).build();
        startForeground(5, notification);
    }

    @Override
    public void onDestroy() {
        cdt.cancel();

        Log.d("tag", "onDestroy: ");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        cdt = new CountDownTimer(300000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("tag", "onTick: " + millisUntilFinished);
                /*** Update Notification Content  ****/
                builder.setContentText(TimerFormat.getTimeString(millisUntilFinished));
                notificationManager.notify(5, builder.build());
            }

            @Override
            public void onFinish() {
                //notificationManager.cancel(5);
            }
        };

        cdt.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
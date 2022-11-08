package com.run_service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class J_service extends Service {

    public static final String CHANNEL_ID =  "notification";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void create_notification_channel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new  NotificationChannel(CHANNEL_ID, "HEARTBEAT", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("CHANNEL DESCRIPTION");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        create_notification_channel();
        Log.d("Tag" , "Starting Notification...");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new  Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle("Title")
                    .setContentText("Text...")
                    .setSmallIcon(R.drawable.pic)
                    .build();
            startForeground(1, notification);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("Tag" , "Service is dead");

    }
    @Override
    public  int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Tag", "Service running creating new thread for the service");
        new Thread(() -> {
            int i = 0;
            while(true){
                try {
                    Thread.sleep(5000);
                    Log.d("Tag" , "service " + i);
                    Log.d("Tag", "Service thread " + Thread.currentThread().getId());
                } catch (InterruptedException e){
                    Log.i("Error", "Fucked Error occured" );
                }
                i++;
            }
        }).start();
        return START_STICKY;
    }
}
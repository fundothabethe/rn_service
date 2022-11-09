package com.run_service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.widget.Toast;
import android.location.Location;
import androidx.annotation.Nullable;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class J_service extends Service {
    private FusedLocationProviderClient fusedLocationClient;
    public static final String CHANNEL_ID =  "notification";
    public static Boolean run_procecss = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void create_notification_channel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new  NotificationChannel(CHANNEL_ID, "HEARTBEAT",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("CHANNEL DESCRIPTION");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void create_location_instance(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
    }
    @Override
    public void onCreate() {
        super.onCreate();
        create_notification_channel();
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
        Toast.makeText(getApplicationContext(), "service Stoped", 1000 * 5).show();
    }
    @Override
    public  int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "Service is starting", 1000 * 5).show();
        new Thread(() -> {
            int i = 0;
            while(run_procecss){
                try {
                    Toast.makeText(getApplicationContext(),
                            "Current location is " + fusedLocationClient.getLastLocation(),
                            1000 * 5).show();
                    Thread.sleep(5000);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("message");
                    myRef.setValue("Hello, World! " + i);
                } catch (InterruptedException e){
                    Toast.makeText(getApplicationContext(), "Fucked Error occured", 1000 * 5).show();
                }
                i++;
            }
        }).start();
        return START_STICKY;
    }
}
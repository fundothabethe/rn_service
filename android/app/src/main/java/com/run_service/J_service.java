package com.run_service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class J_service extends Service {

  private FusedLocationProviderClient fusedLocationClient;
  public static final String CHANNEL_ID = "notification";
  public static Boolean run_procecss = true;
  private Double latitude;
  private Double longitude;

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private void create_notification_channel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
        CHANNEL_ID,
        "HEARTBEAT",
        NotificationManager.IMPORTANCE_DEFAULT
      );
      channel.setDescription("CHANNEL DESCRIPTION");
      NotificationManager notificationManager = getSystemService(
        NotificationManager.class
      );
      notificationManager.createNotificationChannel(channel);
    }
  }

  @Override
  public void onCreate() {
    super.onCreate();
    create_notification_channel();
    fusedLocationClient =
      LocationServices.getFusedLocationProviderClient(getApplicationContext());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      Notification notification = new Notification.Builder(
        getApplicationContext(),
        CHANNEL_ID
      )
        .setContentTitle("Title")
        .setContentText("Text...")
        .setSmallIcon(R.drawable.pic)
        .build();
      startForeground(1, notification);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    run_procecss = false;
    Toast.makeText(getApplicationContext(), "service Stoped", 1000 * 5).show();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    new Thread(() -> {
      int i = 0;
      String user_id;

      while (run_procecss) {
        try {
          Thread.sleep(5000);
          // Toast.makeText(getApplicationContext(), "User id " + user_id, 1000 * 5).show();
          FirebaseDatabase database = FirebaseDatabase.getInstance();
          DatabaseReference myRef = database.getReference("message");
          myRef.setValue("Hello, World! " + i);
   
      if (intent != null && intent.getExtras() != null) {
        user_id = intent.getExtras().getString("user_id");
        Toast
          .makeText(getApplicationContext(), "intent data " + user_id, 1000 * 5)
          .show();
`   
        fusedLocationClient
          .getLastLocation()
          .addOnCompleteListener(
            new OnCompleteListener<Location>() {
              @Override
              public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) { // Check if coords are similar t last coords
                  latitude = location.getLatitude();
                  longitude = location.getLongitude();

                  //   Toast.makeText(getReactApplicationContext(), "Location is " + location.getLatitude(), 5000).show();
                } else {
                  //   callback.invoke("error");
                }
              }
            }
          );
      } else {
        Toast
          .makeText(getApplicationContext(), "No intent data", 1000 * 5)
          .show();
      }
           } catch (InterruptedException e) {
          Toast
            .makeText(
              getApplicationContext(),
              "Fucked Error occured" + e,
              1000 * 5
            )
            .show();
        }
        i++;
      }

    })
      .start();
    return START_STICKY;
  }

  public void get_location(String user_id, Callback callback) {
    fusedLocationClient
      .getLastLocation()
      .addOnCompleteListener(
        new OnCompleteListener<Location>() {
          @Override
          public void onComplete(@NonNull Task<Location> task) {
            Location location = task.getResult();
            if (location != null) {
              //callback.invoke(location);
              //   callback.invoke(location.getLatitude());
              //   Toast.makeText(getReactApplicationContext(), "Location is " + location.getLatitude(), 5000).show();
            } else {
              //   callback.invoke("error");
            }
          }
        }
      );
  }
}

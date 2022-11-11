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

  public static final String CHANNEL_ID = "notification";
  public static Boolean run_procecss = true;
  private FusedLocationProviderClient fusedLocationClient;
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
    if (
      intent != null &&
      run_procecss &&
      intent.getExtras() != null &&
      intent.getExtras().getString("user_id") != null
    ) {
      String user_id = intent.getExtras().getString("user_id");
      FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference myRef = database.getReference(
        user_id + "/user_location"
      );
      new Thread(() -> {
        int i = 0;
        while (run_procecss) {
          try {
            Thread.sleep(5000);
            myRef.child("count").setValue(i);
            fusedLocationClient
              .getLastLocation()
              .addOnCompleteListener(
                new OnCompleteListener<Location>() {
                  @Override
                  public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                      latitude = location.getLatitude();
                      longitude = location.getLongitude();
                      myRef.child("latitude").setValue(latitude);
                      myRef.child("longitude").setValue(longitude);
                    }
                  }
                }
              );
          } catch (InterruptedException e) {
            Toast.makeText(
              getApplicationContext(),
              "Fucked Error occured",
              1000 * 5
            );
          }
          i++;
        }
      })
        .start();
    }
    return START_STICKY;
  }
}

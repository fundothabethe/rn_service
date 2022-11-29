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
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
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
  public Double latitude;
  public Double longitude;
  private LocationCallback location_callback;
  private LocationRequest locationRequest;

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

    locationRequest = locationRequest.create();
    locationRequest.setInterval(100);
    locationRequest.setFastestInterval(50);
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    location_callback =
      new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult location_result) {
          if (location_result != null) {
            for (Location location : location_result.getLocations()) {
              latitude = location.getLatitude();
              longitude = location.getLongitude();
            }
          } else {
            return;
          }
        }
      };

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
    run_procecss = false;
    fusedLocationClient.removeLocationUpdates(location_callback);
    super.onDestroy();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (
      intent != null &&
      intent.getExtras() != null &&
      intent.getExtras().getString("user_id") != null
    ) {
      run_procecss = true;
      String user_id = intent.getExtras().getString("user_id");
      FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference myRef = database.getReference(
        user_id + "/user_location"
      );
      new Thread(() -> {
        int i = 0;
        fusedLocationClient.requestLocationUpdates(
          locationRequest,
          location_callback,
          Looper.getMainLooper()
        );
        while (run_procecss) {
          try {
            Thread.sleep(1000);
            myRef.child("latitude").setValue(latitude);
            myRef.child("longitude").setValue(longitude);
            myRef.child("number").setValue(i);
          } catch (InterruptedException e) {}
          i++;
        }
      })
        .start();
    }
    return START_STICKY;
  }
}

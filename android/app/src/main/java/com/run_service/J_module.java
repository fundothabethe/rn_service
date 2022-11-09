package com.run_service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import javax.annotation.Nonnull;

public class J_module extends ReactContextBaseJavaModule {

  public static final String REACT_CLASS = "Module";
  private static ReactApplicationContext reactContext;
  public Intent service_intent;
  public static final String CHANNEL_ID = "notification";
  private FusedLocationProviderClient fusedLocationClient;

  public J_module(ReactApplicationContext context) {
    super(context);
    reactContext = context;
    service_intent = new Intent(context, J_service.class);
    fusedLocationClient =
      LocationServices.getFusedLocationProviderClient(context);
  }

  @Nonnull
  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @ReactMethod
  public void start_service(String user_id, Promise promise) {
    try {
      service_intent.putExtra("user_id", user_id);
      reactContext.startService(service_intent);
      promise.resolve("Successfully");
    } catch (Exception e) {
      promise.reject("Error occured ", e);
    }
  }

  @ReactMethod
  public void stop_service(Promise promise) {
    try {
      reactContext.stopService(service_intent);
      promise.resolve("Successfully");
    } catch (Exception e) {
      promise.reject("Error occured ", e);
    }
  }

  @ReactMethod
  public void data(String message, int duration) {
    Toast.makeText(getReactApplicationContext(), message, duration).show();
  }

  @ReactMethod
  public void get_location(String user_id, Callback callback) {
    Toast
      .makeText(getReactApplicationContext(), "User id " + user_id, 5000)
      .show();
    fusedLocationClient
      .getLastLocation()
      .addOnCompleteListener(
        new OnCompleteListener<Location>() {
          @Override
          public void onComplete(@NonNull Task<Location> task) {
            Location location = task.getResult();
            if (location != null) {
              //callback.invoke(location);
              callback.invoke(location.getLatitude());
              Toast
                .makeText(
                  getReactApplicationContext(),
                  "Location is " + location.getLatitude(),
                  5000
                )
                .show();
            } else {
              callback.invoke("error");
            }
          }
        }
      );
  }
}

package com.run_service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
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

  public Intent service_intent;
  private LocationCallback location_callback;
  public static final String REACT_CLASS = "Module";
  private static ReactApplicationContext reactContext;
  public static final String CHANNEL_ID = "notification";
  private FusedLocationProviderClient fusedLocationClient;

  LocationRequest locationRequest;

  public J_module(ReactApplicationContext context) {
    super(context);
    reactContext = context;
    service_intent = new Intent(context, J_service.class);
    fusedLocationClient =
      LocationServices.getFusedLocationProviderClient(context);

    // initiate the location request

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
              // latitude = location.getLatitude();
              // longitude = location.getLongitude();

              Toast
                .makeText(
                  getReactApplicationContext(),
                  "location " +
                  location.getLatitude() +
                  "longitude " +
                  location.getLongitude(),
                  5000
                )
                .show();
            }
          } else {
            Toast.makeText(getReactApplicationContext(), "Error ", 5000).show();
          }
        }
      };
  }

  @Nonnull
  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @ReactMethod
  public void start_service(String user_id, Promise promise) {
    try {
      if (isMyServiceRunning(J_service.class)) {
        Toast
          .makeText(
            getReactApplicationContext(),
            "Service already running",
            5000
          )
          .show();
      } else {
        service_intent.putExtra("user_id", user_id);
        reactContext.startService(service_intent);
        Toast
          .makeText(getReactApplicationContext(), "Starting service", 5000)
          .show();
      }
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

  // Check is service is alreaddy running
  private boolean isMyServiceRunning(Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) reactContext.getSystemService(
      Context.ACTIVITY_SERVICE
    );
    boolean service_running = false;

    for (RunningServiceInfo service : manager.getRunningServices(
      Integer.MAX_VALUE
    )) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        service_running = true;
      }
    }
    return service_running;
  }

  @ReactMethod
  public void is_service_running(Promise promise) {
    try {
      Toast
        .makeText(getReactApplicationContext(), "Checking service status", 5000)
        .show();
      if (isMyServiceRunning(J_service.class)) {
        Toast
          .makeText(getReactApplicationContext(), "Service is running", 5000)
          .show();
        promise.resolve("Running");
      } else {
        Toast
          .makeText(
            getReactApplicationContext(),
            "Service is not running",
            5000
          )
          .show();
        promise.resolve("Not running");
      }
    } catch (Exception e) {
      // error.resolve(e);
      Toast
        .makeText(
          getReactApplicationContext(),
          "Error checking service status",
          5000
        )
        .show();
    }
  }

  @ReactMethod
  public void get_location(String user_id, Callback callback) {
    // Initiate the location calllback

    fusedLocationClient.requestLocationUpdates(
      locationRequest,
      location_callback,
      Looper.getMainLooper()
    );
    callback.invoke("latitude: ");
  }

  @ReactMethod
  public void stop_location_updates() {
    fusedLocationClient.removeLocationUpdates(location_callback);
  }
}

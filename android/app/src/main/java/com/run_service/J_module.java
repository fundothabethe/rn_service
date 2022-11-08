package com.run_service;

import android.content.Intent;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.util.Log;
import javax.annotation.Nonnull;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import android.widget.Toast;

import android.app.Notification;
import android.os.Build;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class J_module extends ReactContextBaseJavaModule {
  public static final String REACT_CLASS = "Module";
  private static ReactApplicationContext reactContext;
  public Intent service_intent;
  public static final String CHANNEL_ID =  "notification";

  public J_module(ReactApplicationContext context) {
    super(context);
    reactContext = context;
    service_intent = new Intent(context, J_service.class);
  }

  @Nonnull
  @Override
  public String getName() {
    return REACT_CLASS;
  }
  
  @ReactMethod
  public void start_service(Promise promise) {
    try {
      reactContext.startService(service_intent);
      promise.resolve("Successfully");
    }catch (Exception e){
      promise.reject("Error occured " , e);
    }
   }
  @ReactMethod
  public void stop_service(Promise promise ){
    try {
      reactContext.stopService(service_intent);
      promise.resolve("Successfully");
    }catch (Exception e){
      promise.reject("Error occured " , e);
    }
  }
  @ReactMethod
  public void data(String message, int duration) {
    Toast.makeText(getReactApplicationContext(), message, duration).show();
  }
}
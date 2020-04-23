package com.alinz.parkerdan.shareextension;

import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import android.graphics.Bitmap;
import java.io.InputStream;
import android.util.Log;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ShareModule extends ReactContextBaseJavaModule {

  private Context mContext;
  public ShareModule(ReactApplicationContext reactContext) {
      super(reactContext);
      mContext = reactContext;
  }

  @Override
  public String getName() {
      return "ReactNativeShareExtension";
  }

  @ReactMethod
  public void close() {
    getCurrentActivity().finish();
  }

  @ReactMethod
  public void data(Promise promise) {
      promise.resolve(processIntent());
  }

  public WritableMap processIntent() {
      WritableMap map = Arguments.createMap();

      String value = "";
      String type = "";
      String action = "";

      Activity currentActivity = getCurrentActivity();

      if (currentActivity != null) {
        Intent intent = currentActivity.getIntent();
        action = intent.getAction();
        type = intent.getType();
        if (type == null) {
          type = "";
        }

        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
          value = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        else if (Intent.ACTION_SEND.equals(action) && 
        ("image/*".equals(type) || 
        "image/jpeg".equals(type) || 
        "image/png".equals(type) || 
        "image/jpg".equals(type) || 
        "audio/mpeg".equals(type) || 
        "video/mp4".equals(type) || 
        "application/pdf".equals(type) || 
        "application/msword".equals(type) || 
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(type) || 
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(type) || 
        "application/vnd.ms-exce".equals(type) || 
        "application/epub+zip".equals(type) ) ) {

          Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
          
          value = RealPathUtil.getRealPathFromURI(currentActivity, uri);

       } else {
         
         value = "";
       }
      } else {
        value = "";
        type = "";
      }

      map.putString("type", type);
      map.putString("value",value);

      return map;
  }
}

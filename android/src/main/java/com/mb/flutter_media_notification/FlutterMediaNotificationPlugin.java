package com.mb.flutter_media_notification;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
//imported to build plugin
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/** FlutterMediaNotificationPlugin */
public class FlutterMediaNotificationPlugin implements MethodCallHandler {
  private static final String CHANNEL_ID = "AH Playback";
  private static Registrar registrar;
  private static NotificationPanel nPanel;
  private static MethodChannel channel;

  
  private FlutterMediaNotificationPlugin(Registrar r) {
    registrar = r;
  }
  
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    // final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_media_notification");
    // channel.setMethodCallHandler(new FlutterMediaNotificationPlugin(registrar));
    
    FlutterMediaNotificationPlugin.channel = new MethodChannel(registrar.messenger(), "com.mb.flutter_media_notification");
    FlutterMediaNotificationPlugin.channel.setMethodCallHandler(new FlutterMediaNotificationPlugin(registrar));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    // if (call.method.equals("getPlatformVersion")) {
    //   result.success("Android " + android.os.Build.VERSION.RELEASE);
    // } else {
    //   result.notImplemented();
    // }
    
      switch (call.method) {
          case "show":
              final String title = call.argument("title");
              final String author = call.argument("author");
              final boolean play = call.argument("play");
              show(title, author, play);
              result.success(null);
              break;
            case "hide":
              hide();
              result.success(null);
              break;
          default:
              result.notImplemented();
      }
  }
  
  public static void callEvent(String event) {

    FlutterMediaNotificationPlugin.channel.invokeMethod(event, null, new Result() {
          @Override
          public void success(Object o) {
              // this will be called with o = "some string"
          }

          @Override
          public void error(String s, String s1, Object o) {}

          @Override
          public void notImplemented() {}
      });
  }

  public static void show(String title, String author, boolean play) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          int importance = NotificationManager.IMPORTANCE_DEFAULT;
          NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
          channel.setDescription("Shows notification for currently playing songs.");
          channel.enableVibration(false);
          channel.setSound(null, null);
          NotificationManager notificationManager = registrar.context().getSystemService(NotificationManager.class);
          notificationManager.createNotificationChannel(channel);
      }

      nPanel = new NotificationPanel(registrar.context(), title, author, play);
  }

  private void hide() {
    if(nPanel != null) nPanel.notificationCancel();
    else System.out.println("No any media notification");
  }


}

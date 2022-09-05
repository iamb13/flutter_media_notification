package com.mb.flutter_media_notification;

import androidx.annotation.NonNull;

import android.content.Context;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
//imported to build plugin
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * FlutterMediaNotificationPlugin
 */
public class FlutterMediaNotificationPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
//    private MethodChannel channel;
    private static MethodChannel channel;
    private static final String CHANNEL_ID = "MB Playback";
    private static NotificationPanel nPanel;

    private static Context context;

    /**
     * Plugin registration.
     */
    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        // TODO: your plugin is now attached to a Flutter experience.

        // final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_media_notification");
        // channel.setMethodCallHandler(new FlutterMediaNotificationPlugin(registrar));

        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "com.mb.flutter_media_notification");
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
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

        channel.invokeMethod(event, null, new Result() {
            @Override
            public void success(Object o) {
                // this will be called with o = "some string"
            }

            @Override
            public void error(String s, String s1, Object o) {
            }

            @Override
            public void notImplemented() {
            }
        });
    }

    public static void show(String title, String author, boolean play) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
            channel.setDescription("Shows notification for currently playing songs.");
            channel.enableVibration(false);
            channel.setSound(null, null);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        nPanel = new NotificationPanel(context, title, author, play);
    }

    private void hide() {
        if (nPanel != null) nPanel.notificationCancel();
        else System.out.println("No any media notification");
    }


    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        // TODO: your plugin is no longer attached to a Flutter experience.
        channel.setMethodCallHandler(null);
    }

}

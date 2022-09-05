package com.mb.flutter_media_notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;

/**
 * @iamb13
 * notification panel
 */

class NotificationPanel {
    private Context parent;
    private NotificationManager nManager;
    private String title;
    private String author;
    private boolean play;

    private NotificationCompat.Builder nBuilder;
    private RemoteViews remoteView;

    public NotificationPanel(Context parent, String title, String author, boolean play) {
        this.parent = parent;
        this.title = title;
        this.author = author;
        this.play = play;

         nBuilder = new NotificationCompat.Builder(parent, "MB Playback")
                 .setSmallIcon(R.drawable.ic_stat_music_note)
                 .setPriority(Notification.PRIORITY_DEFAULT)
                 .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                 .setOngoing(this.play)
                 .setOnlyAlertOnce(true)
                 .setVibrate(new long[]{0L})
                 .setSound(null);
        
//        nBuilder = new NotificationCompat.Builder(parent, "AH Playback")
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setSmallIcon(R.drawable.ic_stat_music_note)
////                .setLargeIcon()
////                .setProgress()
////                .setStyle(new NotificationCompat.MediaStyle())
//                .setPriority(Notification.PRIORITY_MAX)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .setOngoing(this.play)
//                .setOnlyAlertOnce(true)
//                .setVibrate(new long[]{0L})
//                .setSound(null);

        remoteView = new RemoteViews(parent.getPackageName(), R.layout.notificationlayout);

        remoteView.setTextViewText(R.id.title, title);
        remoteView.setTextViewText(R.id.author, author);

        if (this.play) {
            remoteView.setImageViewResource(R.id.toggle, R.drawable.baseline_pause_black_48);
        } else {
            remoteView.setImageViewResource(R.id.toggle, R.drawable.baseline_play_arrow_black_48);
        }

        setListeners(remoteView);
        nBuilder.setContent(remoteView);

        Notification notification = nBuilder.build();

        nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(1, notification);
    }

    public void setListeners(RemoteViews view){
        // play / pause song
        Intent intent = new Intent(parent, NotificationReturnSlot.class)
            .setAction("toggle")
            .putExtra("title", this.title)
            .putExtra("author", this.author)
            .putExtra("action", !this.play ? "play" : "pause");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(parent, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.toggle, pendingIntent);

        // next song click action
        Intent nextIntent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("next");
        PendingIntent pendingNextIntent = PendingIntent.getBroadcast(parent, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.next, pendingNextIntent);

        // previous song cick action
        Intent prevIntent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("prev");
        PendingIntent pendingPrevIntent = PendingIntent.getBroadcast(parent, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.prev, pendingPrevIntent);

        // action for selecting song on notification
        Intent selectIntent = new Intent(parent, NotificationReturnSlot.class)
                .setAction("select");
        PendingIntent selectPendingIntent = PendingIntent.getBroadcast(parent, 0, selectIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        view.setOnClickPendingIntent(R.id.layout, selectPendingIntent);
    }


    public void notificationCancel() {
        // nManager.cancel(1);
       try {
           nManager.cancel(1);
       } catch (Exception e) {
        //    System.out.println("Error for notification panel "+ nManager +" ");
       }
    }
}


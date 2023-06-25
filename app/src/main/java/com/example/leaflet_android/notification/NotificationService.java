package com.example.leaflet_android.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.leaflet_android.MainActivity;
import com.example.leaflet_android.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "CHANNEL_ID";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("NotificationService", "Message data payload: " + remoteMessage.getData());
            String messageTitle = "New Message";
            String messageBody = "You have received a new message";

            // Assuming that 'title' and 'body' are keys in your data payload
            if(remoteMessage.getData().containsKey("title")){
                messageTitle = remoteMessage.getData().get("title");
            }
            if(remoteMessage.getData().containsKey("body")){
                messageBody = remoteMessage.getData().get("body");
            }
            sendNotification(messageTitle, messageBody);

            Intent intent = new Intent("MyCustomEvent");
            intent.putExtra("message", remoteMessage.getData().get("message"));  // assuming the message body is located here
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = CHANNEL_ID;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.fall)  // set the icon to use in the notification
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel for leaflet",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}

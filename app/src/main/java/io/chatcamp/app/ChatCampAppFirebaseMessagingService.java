package io.chatcamp.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.chatcamp.sdk.GroupChannelListQuery;
import io.chatcamp.sdk.Message;

/**
 * Created by ChatCamp Team on 07/12/17.
 */

public class ChatCampAppFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String channelId = remoteMessage.getData().get("channelId");
        String channelType = remoteMessage.getData().get("channelType");
        String data = remoteMessage.getData().get("message");
        Message message = Message.createfromSerializedData(data);
        String serverType = remoteMessage.getData().get("server");
        sendNotification(this, channelId, channelType, message,serverType);
        Log.e("PUSH NOTIFICATION", "push notification");
    }

    public static void sendNotification(Context context, String channelId, String channelType, Message message, String serverType) {
        if(!serverType.equalsIgnoreCase("Chatcamp")) {
            // this notification is not sent from Chatcamp
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        final String CHANNEL_ID = "CHANNEL_ID";
        if (Build.VERSION.SDK_INT >= 26) {  // Build.VERSION_CODES.O
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent intent = new Intent(context, ConversationActivity.class);
        String participantState = GroupChannelListQuery.ParticipantState.ALL.name();
        intent.putExtra("channelId", channelId);
        intent.putExtra("channelType", channelType);
        intent.putExtra("participantState", participantState);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_default_contact)
                .setColor(Color.parseColor("#7469C4"))  // small icon background color
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_default_contact))
                .setContentTitle(message.getUser().getDisplayName())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        if (message.getType().equals("attachment")) {
            if (message.getAttachment().getType().contains("image")) {
                notificationBuilder.setContentText("Image");
            } else if (message.getAttachment().getType().contains("video")) {
                notificationBuilder.setContentText("video");
            }  else if (message.getAttachment().getType().contains("application") || message.getAttachment().getType().contains("css") ||
                    message.getAttachment().getType().contains("csv") || message.getAttachment().getType().contains("text")) {
                notificationBuilder.setContentText("document");
            }
        } else if(message.getType().equals("text")) {
            notificationBuilder.setContentText(message.getText());
        } else {
            notificationBuilder.setContentText("new Message");
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

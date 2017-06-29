package creationsofali.teknogia.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import creationsofali.teknogia.R;
import creationsofali.teknogia.activities.MainActivity;
import creationsofali.teknogia.datamodels.RequestCode;

/**
 * Created by ali on 6/2/17.
 */

public class TeknogiaMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived:message = " + remoteMessage.getNotification().getBody());

        // show notification
        onCreateNotification(remoteMessage);
    }

    private void onCreateNotification(RemoteMessage message) {
        Intent postIntent = new Intent(getApplicationContext(), MainActivity.class);
        Map<String, String> data = message.getData();

        String newPostUrl = data.get("url");
        Log.d(TAG, "onCreateNotification:newPostUrl = " + newPostUrl);
        postIntent.putExtra("newPostUrl", newPostUrl);
        postIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultIntent = PendingIntent.getActivities(
                this,
                RequestCode.RC_FCM_NOTIFICATION,
                new Intent[]{postIntent},
                PendingIntent.FLAG_ONE_SHOT);

        // notification sound
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_teknogia)
//                .setTicker("")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.image_teknogia_launcher_round))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody())
                .setSound(notificationSoundUri)
                .setContentIntent(resultIntent)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.color_green));

        NotificationManager nm = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(RequestCode.RC_FCM_NOTIFICATION, notificationBuilder.build());
    }
}

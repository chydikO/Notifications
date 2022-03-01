package com.chudnovskiy.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createNotification(View view) {
        Intent intent = new Intent(getApplicationContext(), FinishActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);


        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, default_notification_channel_id);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setContentTitle(getString(R.string.app_name));
        mBuilder.setContentText("Content to be added");
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.index_1));
        mBuilder.setSmallIcon(R.drawable.index_2);
        mBuilder.setAutoCancel(true);
        mBuilder.setProgress(100, 10, true);
//        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.ladynsax);  //Here is FILE_NAME is the name of file that you want to play
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.setSound(sound, attributes);
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        MediaPlayer mp= MediaPlayer.create(getApplicationContext(), R.raw.ladynsax);
        mp.start();
        Notification build = mBuilder.build();
       // build.flags = build.flags | Notification.FLAG_ONGOING_EVENT; //Notification будет важным и не спрячиться
        build.flags = build.flags | Notification.FLAG_INSISTENT; //Notification будет важным и не спрячиться пока на него не обратишь внимание
        mNotificationManager.notify((int) System.currentTimeMillis(), build);
    }

    public void cancelNotification(View view) {
        mNotificationManager.cancel(Integer.parseInt(NOTIFICATION_CHANNEL_ID));
        Toast.makeText(getApplicationContext(), "cancelNotification", Toast.LENGTH_LONG).show();
    }
}
package p32929.passcodelock.service;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.GnssAntennaInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import p32929.passcodelock.ChangeText;
import p32929.passcodelock.R;

public class SoundService extends Service {
  public static  MediaPlayer ring;
    ChangeText callback;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        ring = MediaPlayer.create(this, R.raw.whistle); //select music file
        ring.setLooping(true); //set looping
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ring.isPlaying()) {
            ring.pause();
            Intent intent1 = new Intent();
            intent1.putExtra("status","0");
            intent.setAction("com.tutorialspoint.CUSTOM_INTENT"); sendBroadcast(intent);
            stopForeground(true);
        } else {
            AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            manager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
            startForeground();
            Intent intent1 = new Intent();
            intent1.putExtra("status","1");
            intent.setAction("com.tutorialspoint.CUSTOM_INTENT"); sendBroadcast(intent);
            ring.start();
            return super.onStartCommand(intent, flags, startId);
        }
        return SoundService.START_NOT_STICKY;
    }

    public void onDestroy() {
        if (ring != null) {
            ring.release();
        }
        super.onDestroy();
    }

    private void startForeground() {
        String channelId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel("my_service", "My Background Service");
        } else {
            channelId = "";
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)

        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(101, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName) {
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

}

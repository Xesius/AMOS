
package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyService extends Service
{
    private static final String TAG = "My Service";
    private MediaPlayer mediaPlayer;
    private boolean playing = false;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!playing) {
            mediaPlayer = MediaPlayer.create(MyService.this, R.raw.music);
            mediaPlayer.start();

            mediaPlayer.setLooping(true);
            playing = true;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;  //???
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
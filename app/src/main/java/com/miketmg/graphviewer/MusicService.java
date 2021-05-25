package com.miketmg.graphviewer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {

    MediaPlayer mediaPlayer;

    public MusicService() { }

    @Override
    public void onCreate() {
        super.onCreate();
        startMusic();
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    public void startMusic()
    {
        mediaPlayer = MediaPlayer.create(this, R.raw.king);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
}

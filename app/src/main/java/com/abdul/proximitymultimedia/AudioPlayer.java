package com.abdul.proximitymultimedia;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class AudioPlayer extends Service {

    String LOG_TAG = "Media Player";
    public static int CURR_MEDIA_ID = 999;
    MediaPlayer mediaPlayerObj = null;

    /**
     *
     */
    public void onCreate() {
        Log.i(LOG_TAG, "Audio player service started");
        CURR_MEDIA_ID = R.raw.star_wars60;
        mediaPlayerObj = MediaPlayer.create(this, R.raw.star_wars60);
        mediaPlayerObj.setLooping(true);
    }

    /**
     *
     * @param identifier
     */
    public void playSpecific(String identifier) {
        Log.i(LOG_TAG, "Play audio for identifier: "+identifier);
        int resid = -999;
        if(identifier.equals(LocationsEnum.LOCATION_1.getIdentifier())) {
            resid = R.raw.imperial_march60;
        } else {
            resid = R.raw.star_wars60;
        }

        if(CURR_MEDIA_ID == resid){
            // media is already playing let it be
            Log.i(LOG_TAG, "Already playing, let it be.");
        } else {
            // switch media
            Log.i(LOG_TAG, "Playing new media");
            if(mediaPlayerObj.isPlaying()) {
                mediaPlayerObj.stop();
                mediaPlayerObj.release();
            }

            mediaPlayerObj = MediaPlayer.create(this, resid);
            mediaPlayerObj.setLooping(true);
            Log.i(LOG_TAG, "Updating value of CURR MEDIA ID");
            CURR_MEDIA_ID = resid;
        }

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        String identifier = (String) extras.get("identifier");
        playSpecific(identifier);
        mediaPlayerObj.start();
        Log.i(LOG_TAG, "Media player started");

        return Service.START_STICKY;
    }

    public void onStop() {
        mediaPlayerObj.stop();
        mediaPlayerObj.release();
    }

    public void  onPause() {
        mediaPlayerObj.stop();
        mediaPlayerObj.release();
    }

    public void onDestroy() {
        if(mediaPlayerObj != null) {
            mediaPlayerObj.stop();
            mediaPlayerObj.release();
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

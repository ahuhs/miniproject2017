package com.a4dotsinc.profilo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by ARAVIND on 24-03-2018.
 */

public class Timed_Changes extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       /* Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mp = MediaPlayer.create(context, notification);
        mp.start();
        Toast.makeText(context, "Triggered", Toast.LENGTH_SHORT).show();*/
        final AudioManager am = (AudioManager)context.getSystemService(AUDIO_SERVICE);

        if (am.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
        else {
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }
}

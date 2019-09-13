package com.mclk.devamsizliktakibi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class MuteControlReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SharedPreferences settingValues = context.getSharedPreferences("com.mclk.devamsizliktakibi", MODE_PRIVATE);
            SharedPreferences.Editor settingValuesEditor=settingValues.edit();

            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if(settingValues.getBoolean(context.getResources().getResourceEntryName(R.id.switch_sessize_al),true))
            {
                Calendar nowCldr =Calendar.getInstance();
                if(intent.getIntExtra("basSaati",0)==nowCldr.get(Calendar.HOUR_OF_DAY) && intent.getIntExtra("basDakikasi",0)==nowCldr.get(Calendar.MINUTE)  )
                {
                    settingValuesEditor.putInt("lastVoiceValue",am.getStreamVolume(AudioManager.STREAM_RING));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        am.setStreamVolume(AudioManager.STREAM_RING,AudioManager.ADJUST_MUTE,AudioManager.FLAG_VIBRATE);
                    } else {
                        am.setStreamMute(AudioManager.STREAM_RING, true);
                    }
                }
                else if(intent.getIntExtra("bitSaati",0)==nowCldr.get(Calendar.HOUR_OF_DAY) && intent.getIntExtra("bitDakikasi",0)==nowCldr.get(Calendar.MINUTE)  )
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        am.setStreamVolume(AudioManager.STREAM_RING,settingValues.getInt("lastVoiceValue",AudioManager.ADJUST_UNMUTE),0);
                    } else {
                        am.setStreamSolo(AudioManager.STREAM_RING, true);
                    }
                }
            }
        }
        catch (Exception ex)
        {

        }

    }
}

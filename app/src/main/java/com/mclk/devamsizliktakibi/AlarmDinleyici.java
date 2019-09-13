package com.mclk.devamsizliktakibi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class AlarmDinleyici extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SharedPreferences settingValues = context.getSharedPreferences("com.mclk.devamsizliktakibi", MODE_PRIVATE);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            Date nowTime = new Date();

            nowTime.setTime(System.currentTimeMillis());
            Date donemBaslangici,
                    donemBitisi;
            try {
                donemBaslangici = dateFormat.parse(settingValues.getString(context.getResources().getResourceEntryName(R.id.txt_donem_bas_tarihi), dateFormat.format(Calendar.getInstance().getTime())));
                donemBitisi = dateFormat.parse(settingValues.getString(context.getResources().getResourceEntryName(R.id.txt_donem_bitis_tarihi),dateFormat.format(Calendar.getInstance().getTime())));
            }
            catch (Exception ex)
            {
                donemBaslangici=nowTime;
                donemBitisi=nowTime;
            }

            if (nowTime.getTime() > donemBaslangici.getTime() && nowTime.getTime() < donemBitisi.getTime()) {
                int dersId = intent.getIntExtra("dersId", -1);
                dbVeriIslemMerkezi dbveriislemmerkezi = new dbVeriIslemMerkezi(context);
                tblDers TekDersSorgula = dbveriislemmerkezi.TekDersSorgula(dersId);
                if (TekDersSorgula.getKritikSinir() >= 0 && TekDersSorgula.getDevamsizlik() >= TekDersSorgula.getKritikSinir() && TekDersSorgula.getKritikSinir() <= TekDersSorgula.getDevSiniri()) {
                    int alarmId = intent.getIntExtra("alarmId", -1);
                    String dersAdi = TekDersSorgula.getAdi();
                    Intent intentAlarmActivity = new Intent(context, AlarmAnindaAcilan.class);
                    intentAlarmActivity.putExtra("alarmAdi", dersAdi);
                    intentAlarmActivity.putExtra("alarmId", alarmId);
                    intentAlarmActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intentAlarmActivity);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, "Hatalı çalıştı. Hata Mesajı : "+ex.getMessage(), Toast.LENGTH_LONG).show();
    }
    }
}
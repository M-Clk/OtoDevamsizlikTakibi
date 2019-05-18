package com.mclk.devamsizliktakibi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.renderscript.RenderScript;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;

public class UyariDinleyici extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            SharedPreferences settingValues = context.getSharedPreferences("com.mclk.devamsizliktakibi", MODE_PRIVATE);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            //Dönem kontrolü
            Date nowTime = new Date();

            nowTime.setTime(System.currentTimeMillis());
            Date donemBaslangici,
                    donemBitisi;
            String timeParse=nowTime.getDay()+"."+nowTime.getMonth()+"."+nowTime.getYear();
            try {
                donemBaslangici =dateFormat.parse(settingValues.getString(context.getResources().getResourceEntryName(R.id.txt_donem_bas_tarihi), timeParse));
                donemBitisi = dateFormat.parse(settingValues.getString(context.getResources().getResourceEntryName(R.id.txt_donem_bitis_tarihi),timeParse));
            }
            catch (Exception ex)
            {
                donemBaslangici=nowTime;
                donemBitisi=nowTime;
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (nowTime.getTime() <= donemBaslangici.getTime() || nowTime.getTime() >= donemBitisi.getTime()) {
                Toast.makeText(context, "Sanırım dönem aralığını değiştirdiniz. Eğer ders dönemi bitmediyse ayarlarınızı güncelleyin.", Toast.LENGTH_LONG).show();
                return;
            }
            //
            Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            String dersAdi = intent.getStringExtra("dersAdi");
            String dersSaati = intent.getStringExtra("dersSaati");
            int dersId = intent.getIntExtra("dersId", 0);
            int notificationId = intent.getIntExtra("NOTIFICATION_ID", 0);

            Intent intentDevGuncelle = new Intent(context, DevGuncelle.class);
            intentDevGuncelle.putExtra("NOTIFICATION_ID", notificationId);
            intentDevGuncelle.putExtra("dersId", dersId);

            PendingIntent derseGitmedi = PendingIntent.getActivity(context, notificationId, intentDevGuncelle, 0);

            Intent intentNotifyKapat = new Intent(context, NotificationKapat.class);
            intentNotifyKapat.putExtra("NOTIFICATION_ID", notificationId);
            intentNotifyKapat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent derseGitti = PendingIntent.getActivity(context, notificationId, intentNotifyKapat, 0);

            Intent intentDersTatildi = new Intent(context, Tatildi.class);
            intentDersTatildi.putExtra("NOTIFICATION_ID", notificationId);
            intentDersTatildi.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent dersTatildi = PendingIntent.getActivity(context, notificationId, intentDersTatildi, 0);

            String mesaj = "Bu gün ("+dersSaati+") saatleri arasındaki "+dersAdi+" dersine gittiniz mi?";

            String title="Devamsızlık Takibi";
            NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.ic_launcher);
                    builder.setContentTitle(title)
                    .setColorized(false)
                    .setContentText(mesaj)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(false)
                    .setOngoing(true)
                    .addAction(R.drawable.ic_check_circle_black_24dp, "Evet",derseGitti )
                    .addAction(R.drawable.ic_cancel_black_24dp, "Hayır", derseGitmedi)
                    .addAction(R.drawable.ic_do_not_disturb_on_black_24dp, "Tatildi", dersTatildi)
                    .setVibrate(new long[]{500, 1000})
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(mesaj).setBigContentTitle(title))
                    .setSound(defaultUri);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                String channelId = "com.mclk.devamsizliktakibi";
                NotificationChannel channel = new NotificationChannel(channelId,
                            "Devamsızlık Takibi İletişim Kanalı", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            } notificationManager.notify(notificationId,builder.build());
        }
        catch (Exception ex)
        {
            Log.d("Hata Mesajı",ex.getMessage());
        }

    }
}

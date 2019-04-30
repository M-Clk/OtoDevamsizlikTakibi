package com.mclk.devamsizliktakibi;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class DevGuncelle extends AppCompatActivity {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_dev_guncelle);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = getIntent();
        try {
           // dbVeriIslemMerkezi dbveriislemmerkezi = new dbVeriIslemMerkezi(this);
            //dbveriislemmerkezi.Ac();
            double DevamsizlikArttir = 1;/*dbveriislemmerkezi.DevamsizlikArttir(intent.getIntExtra("dersId", 0));*/
            notificationManager.cancel(intent.getIntExtra("NOTIFICATION_ID", 0));
            if (DevamsizlikArttir > 0.0d) {
                Toast.makeText(this, "Bu dersin devamsızlığı "+Double.toString(DevamsizlikArttir)+" hafta oldu.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Bu dersin devamsızlığı değiştirlemedi. Manuel olarak değiştirmeyi deneyin.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {


            Toast.makeText(this, "Bir sorun çıktı. ", Toast.LENGTH_LONG).show();
        } catch (Throwable th) {
            finishAndRemoveTask();
        }
        finishAndRemoveTask();
    }
}
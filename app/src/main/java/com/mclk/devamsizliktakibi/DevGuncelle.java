package com.mclk.devamsizliktakibi;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class DevGuncelle extends AppCompatActivity {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_dev_guncelle);
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = getIntent();
        int dersId = intent.getIntExtra("dersId",0);
       final  int notifyId = intent.getIntExtra("NOTIFICATION_ID", 0);
        try {
          dbVeriIslemMerkezi dbveriislemmerkezi = new dbVeriIslemMerkezi(this);
            String message = dbveriislemmerkezi.DevamsizlikArttir(dersId);
            if (message!=null) {
                tblDers ders = dbveriislemmerkezi.TekDersSorgula(dersId);
                AlertDialog.Builder ConfirmDialog = new AlertDialog.Builder(this);
                ConfirmDialog.setTitle("Devamsızlık Raporu");
                ConfirmDialog.setMessage(message+DevBilgisi.MesajVer(ders));
                ConfirmDialog.setIcon(R.drawable.ic_info_black_24dp);
                ConfirmDialog.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        finishAndRemoveTask();
                        notificationManager.cancel(notifyId);
                    }
                });
                ConfirmDialog.show();

            } else {
                Toast.makeText(this, "Bu dersin devamsızlığı değiştirlemedi. Manuel olarak değiştirmeyi deneyin.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Bir sorun çıktı. Hata mesajı:\n"+ex.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Throwable th) {
            finishAndRemoveTask();
        }
    }
}
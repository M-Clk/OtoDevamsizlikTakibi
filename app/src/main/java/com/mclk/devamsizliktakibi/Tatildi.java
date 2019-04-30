package com.mclk.devamsizliktakibi;


import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Tatildi extends AppCompatActivity {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_tatildi);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Toast.makeText(this, "İyi tatiller. Devamsızlığınıza işlenmedi.", Toast.LENGTH_LONG).show();
        notificationManager.cancel(getIntent().getIntExtra("NOTIFICATION_ID", 0));
        finishAndRemoveTask();
    }
}

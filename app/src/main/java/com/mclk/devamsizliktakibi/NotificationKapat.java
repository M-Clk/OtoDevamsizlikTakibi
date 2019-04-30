package com.mclk.devamsizliktakibi;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class NotificationKapat extends AppCompatActivity {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_notification_kapat);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Toast.makeText(this, "Umarım yerine imza attırmamışsındır!", Toast.LENGTH_SHORT).show();
        notificationManager.cancel(getIntent().getIntExtra("NOTIFICATION_ID", 0));
        finishAndRemoveTask();
    }
}

package com.mclk.devamsizliktakibi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class InitialSettings extends Service {
    dbVeriIslemMerkezi dbIslem;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.dbIslem = new dbVeriIslemMerkezi(this);
        this.dbIslem.bildirimVeAlarmYukle(this);
    }
}

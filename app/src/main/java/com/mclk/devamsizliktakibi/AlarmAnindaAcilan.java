package com.mclk.devamsizliktakibi;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlarmAnindaAcilan extends AppCompatActivity {
    String alarmAdi;
    int alarmId;
    boolean baslat = false;
    ImageButton btnKontrol;
    int cevap;
    Cursor cursor;
    RingtoneManager manager;
    SharedPreferences settingValues;
    int soruSayisi;
    EditText txtCevap;
    TextView txtSoru;

    public boolean onMenuOpened(int i, Menu menu) {
        return false;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        Toast.makeText(this, "Lütfen soruyu çözün. Uyanmanız gerek.", Toast.LENGTH_SHORT).show();
        return true;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            this.settingValues = getSharedPreferences("com.mclk.devamsizliktakibi", 0);
            ((AudioManager) getSystemService(AUDIO_SERVICE)).setStreamVolume(2, 100, 17);
            requestWindowFeature(1);
            getWindow().setFlags(1024, 1024);
            setContentView(R.layout.activity_alarm_aninda_acilan);
            getWindow().addFlags(6815744);
            this.txtSoru = (TextView) findViewById(R.id.txt_soru);
            this.btnKontrol = (ImageButton) findViewById(R.id.btn_kontrol_et);
            TextView textView = (TextView) findViewById(R.id.txt_bilgilendirme);
            this.txtCevap = (EditText) findViewById(R.id.txt_cevap);
            List<Uri> arrayList = new ArrayList<Uri>();
            this.manager = new RingtoneManager(this);
            this.manager.setType(7);
            this.cursor = this.manager.getCursor();
            this.cursor.moveToFirst();
            do {
                arrayList.add(this.manager.getRingtoneUri(this.cursor.getPosition()));
            } while (this.cursor.moveToNext());
            Intent intent = getIntent();
            this.alarmId = intent.getIntExtra("alarmId", 0);
            this.alarmAdi = intent.getStringExtra("alarmAdi");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.alarmAdi);
            stringBuilder.append(" dersine gitmeniz gerek. Uyandığınızdan emin olmamız için bu soruyu çözmeniz gerek.");
            textView.setText(stringBuilder.toString());
            this.manager.getRingtone(this.settingValues.getInt("alarmSesi", 0)).play();
            final int i = this.settingValues.getInt("zorlukDerecesi", 0);
            this.cevap = SoruOlustur(i);
            this.soruSayisi = this.settingValues.getInt("soruSayisi", 0);
            this.btnKontrol.setOnClickListener(new OnClickListener() {
                public void onClick(android.view.View r4) {
                    /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1280851663.run(Unknown Source)
*/
                    /*
                    r3 = this;
                    r4 = 0;
                    r0 = com.mclk.devamsizliktakibi.AlarmAnindaAcilan.this;	 Catch:{ Exception -> 0x006d }
                    r0 = r0.txtCevap;	 Catch:{ Exception -> 0x006d }
                    r0 = r0.getText();	 Catch:{ Exception -> 0x006d }
                    r0 = r0.toString();	 Catch:{ Exception -> 0x006d }
                    r0 = java.lang.Integer.valueOf(r0);	 Catch:{ Exception -> 0x006d }
                    r0 = r0.intValue();	 Catch:{ Exception -> 0x006d }
                    r1 = com.mclk.devamsizliktakibi.AlarmAnindaAcilan.this;	 Catch:{ Exception -> 0x006d }
                    r1 = r1.cevap;	 Catch:{ Exception -> 0x006d }
                    if (r0 != r1) goto L_0x0058;	 Catch:{ Exception -> 0x006d }
                L_0x001b:
                    r0 = com.mclk.devamsizliktakibi.AlarmAnindaAcilan.this;	 Catch:{ Exception -> 0x006d }
                    r1 = r0.soruSayisi;	 Catch:{ Exception -> 0x006d }
                    r1 = r1 + -1;	 Catch:{ Exception -> 0x006d }
                    r0.soruSayisi = r1;	 Catch:{ Exception -> 0x006d }
                    if (r1 != 0) goto L_0x004b;	 Catch:{ Exception -> 0x006d }
                L_0x0025:
                    r0 = com.mclk.devamsizliktakibi.AlarmAnindaAcilan.this;	 Catch:{ Exception -> 0x006d }
                    r0 = r0.manager;	 Catch:{ Exception -> 0x006d }
                    r1 = com.mclk.devamsizliktakibi.AlarmAnindaAcilan.this;	 Catch:{ Exception -> 0x006d }
                    r1 = r1.settingValues;	 Catch:{ Exception -> 0x006d }
                    r2 = "alarmSesi";	 Catch:{ Exception -> 0x006d }
                    r1 = r1.getInt(r2, r4);	 Catch:{ Exception -> 0x006d }
                    r0 = r0.getRingtone(r1);	 Catch:{ Exception -> 0x006d }
                    r0.stop();	 Catch:{ Exception -> 0x006d }
                    r0 = com.mclk.devamsizliktakibi.AlarmAnindaAcilan.this;	 Catch:{ Exception -> 0x006d }
                    r1 = "Hadi derse...";	 Catch:{ Exception -> 0x006d }
                    r0 = android.widget.Toast.makeText(r0, r1, r4);	 Catch:{ Exception -> 0x006d }
                    r0.show();	 Catch:{ Exception -> 0x006d }
                    r0 = com.mclk.devamsizliktakibi.AlarmAnindaAcilan.this;	 Catch:{ Exception -> 0x006d }
                    r0.finishAndRemoveTask();	 Catch:{ Exception -> 0x006d }
                    goto L_0x0078;	 Catch:{ Exception -> 0x006d }
                L_0x004b:
                    r0 = com.mclk.devamsizliktakibi.AlarmAnindaAcilan.this;	 Catch:{ Exception -> 0x006d }
                    r1 = com.mclk.devamsizliktakibi.AlarmAnindaAcilan.this;	 Catch:{ Exception -> 0x006d }
                    r2 = r0;	 Catch:{ Exception -> 0x006d }
                    r1 = r1.SoruOlustur(r2);	 Catch:{ Exception -> 0x006d }
                    r0.cevap = r1;	 Catch:{ Exception -> 0x006d }
                    goto L_0x0078;	 Catch:{ Exception -> 0x006d }
                L_0x0058:
                    r0 = com.mclk.devamsizliktakibi.AlarmAnindaAcilan.this;	 Catch:{ Exception -> 0x006d }
                    r0 = r0.txtCevap;	 Catch:{ Exception -> 0x006d }
                    r1 = "";	 Catch:{ Exception -> 0x006d }
                    r0.setText(r1);	 Catch:{ Exception -> 0x006d }
                    r0 = com.mclk.devamsizliktakibi.AlarmAnindaAcilan.this;	 Catch:{ Exception -> 0x006d }
                    r1 = "Cevabınız doğru değil. Tekrar deneyin.";	 Catch:{ Exception -> 0x006d }
                    r0 = android.widget.Toast.makeText(r0, r1, r4);	 Catch:{ Exception -> 0x006d }
                    r0.show();	 Catch:{ Exception -> 0x006d }
                    goto L_0x0078;
                L_0x006d:
                    r0 = com.mclk.devamsizliktakibi.AlarmAnindaAcilan.this;
                    r1 = "Lütfen sadece sayı girin.";
                    r4 = android.widget.Toast.makeText(r0, r1, r4);
                    r4.show();
                L_0x0078:
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.mclk.devamsizliktakibi.AlarmAnindaAcilan.1.onClick(android.view.View):void");
                }
            });
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Hata Var : ");
            stringBuilder2.append(e.getMessage());
            Toast.makeText(this, stringBuilder2.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(1798);
    }

    protected void onPause() {
        super.onPause();
        ((ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE)).moveTaskToFront(getTaskId(), 0);
    }

    int SoruOlustur(int i) {
        Random random = new Random();
        int i2 = 11;
        int i3 = 10;
        StringBuilder stringBuilder;
        int nextInt;
        TextView textView;
        switch (i) {
            case 0:
                i2 = random.nextInt(100);
                i3 = random.nextInt(100);
                i = random.nextInt(100);
                TextView textView2 = this.txtSoru;
                stringBuilder = new StringBuilder();
                stringBuilder.append(i2);
                stringBuilder.append("+");
                stringBuilder.append(i3);
                stringBuilder.append("+");
                stringBuilder.append(i);
                textView2.setText(stringBuilder.toString());
                return (i2 + i3) + i;
            case 1:
                i = random.nextInt(11) + 1;
                if (i <= 5) {
                    i3 = 15;
                    i2 = 7;
                } else {
                    i2 = 1;
                }
                i3 = random.nextInt(i3) + i2;
                nextInt = random.nextInt(8) + 1;
                textView = this.txtSoru;
                stringBuilder = new StringBuilder();
                stringBuilder.append(i);
                stringBuilder.append("*");
                stringBuilder.append(i3);
                stringBuilder.append("*");
                stringBuilder.append(nextInt);
                textView.setText(stringBuilder.toString());
                return (i * i3) * nextInt;
            case 2:
                int i4;
                int i5;
                int nextInt2 = random.nextInt(99) + 1;
                int nextInt3 = random.nextInt(99) + 1;
                if (nextInt3 <= 10) {
                    i4 = 99;
                    i5 = 10;
                } else {
                    i4 = 11;
                    i5 = 1;
                }
                i4 = random.nextInt(i4) + i5;
                if (nextInt3 <= 10) {
                    i2 = 99;
                } else {
                    i3 = 1;
                }
                i = random.nextInt(99) + 1;
                nextInt = random.nextInt(i2) + i3;
                textView = this.txtSoru;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(nextInt2);
                stringBuilder2.append("+");
                stringBuilder2.append(i4);
                stringBuilder2.append("*");
                stringBuilder2.append(nextInt3);
                stringBuilder2.append("+");
                stringBuilder2.append(nextInt);
                stringBuilder2.append("*");
                stringBuilder2.append(i);
                textView.setText(stringBuilder2.toString());
                return (nextInt2 + (i4 * nextInt3)) + (nextInt * i);
            default:
                return 0;
        }
    }
}

package com.mclk.devamsizliktakibi;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class AlarmAnindaAcilan extends AppCompatActivity implements OnClickListener {
    String alarmAdi;
    int alarmId;
    int soruSayisiIlkDeger;
    ImageButton btnKontrol;
    int cevap;
    RingtoneManager manager;
    SharedPreferences settingValues;
    int soruSayisi;
    EditText txtCevap;
    TextView txtSoru;
    String zorlukDerecesi;
    int alarmSesiId;
    ProgressBar progressBar;

    public boolean onMenuOpened(int i, Menu menu) {
        return false;
    }


    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
            onClick(new View(this));
        else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_0 ||
                keyEvent.getKeyCode() == KeyEvent.KEYCODE_1 ||
                keyEvent.getKeyCode() == KeyEvent.KEYCODE_2 ||
                keyEvent.getKeyCode() == KeyEvent.KEYCODE_3 ||
                keyEvent.getKeyCode() == KeyEvent.KEYCODE_4 ||
                keyEvent.getKeyCode() == KeyEvent.KEYCODE_5 ||
                keyEvent.getKeyCode() == KeyEvent.KEYCODE_6 ||
                keyEvent.getKeyCode() == KeyEvent.KEYCODE_7 ||
                keyEvent.getKeyCode() == KeyEvent.KEYCODE_8 ||
                keyEvent.getKeyCode() == KeyEvent.KEYCODE_9 ||
                keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL)
            super.dispatchKeyEvent(keyEvent);
        else
            Toast.makeText(this, "Doğru sonucu bulduğunuzda otomatik olarak kapanacaktır.", Toast.LENGTH_SHORT).show();
        return true;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            settingValues = getSharedPreferences("com.mclk.devamsizliktakibi", 0);
            ((AudioManager) getSystemService(AUDIO_SERVICE)).setStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_alarm_aninda_acilan);
            getWindow().addFlags(6815744);

            this.txtSoru = (TextView) findViewById(R.id.txt_soru);
            this.btnKontrol = (ImageButton) findViewById(R.id.btn_kontrol_et);
            TextView textView = (TextView) findViewById(R.id.txt_bilgilendirme);
            this.txtCevap = (EditText) findViewById(R.id.txt_cevap);
            progressBar = (ProgressBar) findViewById(R.id.progressBarQuestion);
            this.manager = new RingtoneManager(this);
            this.manager.setType(RingtoneManager.TYPE_ALL);
            Cursor cursor = manager.getCursor();
            cursor.moveToFirst();


            Intent intent = getIntent();
            this.alarmId = intent.getIntExtra("alarmId", 0);
            this.alarmAdi = intent.getStringExtra("alarmAdi");
            textView.setText(alarmAdi + " dersi için uyandığınızdan emin olmalıyız. Bu soruyu çözmeniz gerek.");


            String strWithValue = settingValues.getString(getResources().getResourceEntryName(R.id.text_zorluk), "Toplama İşlemi|" + "top");
            zorlukDerecesi = strWithValue.substring(strWithValue.indexOf('|') + 1);


            strWithValue = settingValues.getString(getResources().getResourceEntryName(R.id.text_soru_sayisi), "2 Soru|" + 2);
            soruSayisi = Integer.valueOf(strWithValue.substring(strWithValue.indexOf('|') + 1));
            soruSayisiIlkDeger=soruSayisi;

            strWithValue = settingValues.getString(getResources().getResourceEntryName(R.id.text_alarm_sesi), "Varsayılan Zil Sesi|" + 0);
            alarmSesiId = Integer.valueOf(strWithValue.substring(strWithValue.indexOf('|') + 1));

            this.cevap = SoruOlustur();
            this.manager.getRingtone(alarmSesiId).play();

            btnKontrol.setOnClickListener(this);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(txtSoru.getWindowToken(),InputMethodManager.SHOW_IMPLICIT,0);
        } catch (Exception ex) {
            Toast.makeText(this, "Hata Var: " + ex.getMessage(), Toast.LENGTH_LONG).show();
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

    int SoruOlustur() {
        Random random = new Random();
        int sayi1 = 11;
        int sayi2 = 10;
        int sayi3;
        int nextInt;
        TextView textView;
        TextView txtSoru = this.txtSoru;
        switch (zorlukDerecesi) {
            case "top":
                sayi1 = random.nextInt(100);
                sayi2 = random.nextInt(100);
                sayi3 = random.nextInt(100);
                txtSoru.setText(sayi1 + "+" + sayi2 + "+" + sayi3);
                return (sayi1 + sayi2) + sayi3;
            case "carp":
                sayi3 = random.nextInt(11) + 1;
                if (sayi3 <= 5) {
                    sayi2 = 15;
                    sayi1 = 7;
                } else {
                    sayi1 = 1;
                }
                sayi2 = random.nextInt(sayi2) + sayi1;
                nextInt = random.nextInt(8) + 1;
                textView = this.txtSoru;
                textView.setText(sayi3 + "*" + sayi2 + "*" + nextInt);
                return (sayi3 * sayi2) * nextInt;
            case "topVeCarp":
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
                    sayi1 = 99;
                } else {
                    sayi2 = 1;
                }
                sayi3 = random.nextInt(99) + 1;
                nextInt = random.nextInt(sayi1) + sayi2;
                textView = this.txtSoru;
                textView.setText(nextInt2 + "+" + i4 + "*" + nextInt3 + "+" + nextInt + "*" + sayi3);
                return nextInt2 + (i4 * nextInt3) + (nextInt * sayi3);
            default:
                return 0;
        }
    }

    @Override
    public void onClick(View view) {

        int sonuc;
        try {
            sonuc = Integer.parseInt(txtCevap.getText().toString());
        } catch (Exception ex) {
            Toast.makeText(this, "Soruyu cevaplayın.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sonuc == cevap)
        {
            soruSayisi--;
            int yuzde = (int)((soruSayisi)*100/soruSayisiIlkDeger);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress((100-yuzde),true);
            }
            else
                progressBar.setProgress((100-yuzde));
        }
        else {
            Toast.makeText(this, "Cevabınız doğru değil. Tekrar deneyin.", Toast.LENGTH_SHORT).show();
        }
        txtCevap.setText("");
        if (soruSayisi == 0) {
            manager.getRingtone(alarmSesiId).stop();
            Toast.makeText(this, "Hadi derse...", Toast.LENGTH_SHORT).show();
            ActivityCompat.finishAffinity(this);
        }
        else if (sonuc == cevap)
            cevap=SoruOlustur();
    }
}

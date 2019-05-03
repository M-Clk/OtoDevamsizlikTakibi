package com.mclk.devamsizliktakibi;

import android.app.Dialog;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmOptionsActivity extends AppCompatActivity {
    Toolbar toolbar;

    TextView txtBaslangicZamani,
    txtZorlukDerecesi,
    txtSoruSayisi,
    txtAlarmSesi;

    Dialog dialogOption;
    String selectedOptionBaslangic = "10 Dakika Önce|" + 10,
           selectedOptionZorluk = "Toplama İşlemi|" + "top",
           selectedOptionSoru = "1 Soru|" + 1,
           selectedOptionAlarm = "Varsayılan Zil Sesi|"+0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_options);

        InitializingComponents();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    void InitializingComponents() {
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle("Alarm Seçenekleri");
        txtBaslangicZamani = (TextView)findViewById(R.id.text_baslangic_zamani);
        txtZorlukDerecesi = (TextView)findViewById(R.id.text_zorluk);
        txtSoruSayisi = (TextView)findViewById(R.id.text_soru_sayisi);
        txtAlarmSesi = (TextView)findViewById(R.id.text_alarm_sesi);
    }
    void LoadLastSettings() {
        String strWithValue;
        strWithValue = MainActivity.settingValues.getString(getResources().getResourceEntryName(txtBaslangicZamani.getId()), selectedOptionBaslangic);
        txtBaslangicZamani.setText(strWithValue.substring(0, strWithValue.indexOf('|')));
        strWithValue = MainActivity.settingValues.getString(getResources().getResourceEntryName(txtZorlukDerecesi.getId()), selectedOptionZorluk);
        txtZorlukDerecesi.setText(strWithValue.substring(0, strWithValue.indexOf('|')));
        strWithValue = MainActivity.settingValues.getString(getResources().getResourceEntryName(txtSoruSayisi.getId()), selectedOptionSoru);
        txtSoruSayisi.setText(strWithValue.substring(0, strWithValue.indexOf('|')));
        strWithValue = MainActivity.settingValues.getString(getResources().getResourceEntryName(txtAlarmSesi.getId()), selectedOptionAlarm);
        txtAlarmSesi.setText(strWithValue.substring(0, strWithValue.indexOf('|')));
    }
    void BtnGuncelleOnClick(View view) {
        if (view.getId() == R.id.btn_alarm_guncelle)

                try {
                    MainActivity.settingValuesEditor.putString(getResources().getResourceEntryName(txtBaslangicZamani.getId()), selectedOptionBaslangic);
                    MainActivity.settingValuesEditor.putString(getResources().getResourceEntryName(txtZorlukDerecesi.getId()), selectedOptionZorluk);
                    MainActivity.settingValuesEditor.putString(getResources().getResourceEntryName(txtSoruSayisi.getId()), selectedOptionSoru);
                    MainActivity.settingValuesEditor.putString(getResources().getResourceEntryName(txtAlarmSesi.getId()), selectedOptionAlarm);
                    MainActivity.settingValuesEditor.apply();
                    Toast.makeText(this, "Veriler güncellendi.", Toast.LENGTH_SHORT).show();
                    this.finish();
                } catch (Exception ex) {

            }
    }

}
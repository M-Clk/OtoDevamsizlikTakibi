package com.mclk.devamsizliktakibi;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AlarmOptionsActivity extends AppCompatActivity {
    Toolbar toolbar;

    TextView txtBaslangicZamani,
    txtZorlukDerecesi,
    txtSoruSayisi,
    txtAlarmSesi;

    Dialog dialogOption;
    String selectedOptionBaslangic = "1 Saat Önce|" + 60,
           selectedOptionZorluk = "Toplama İşlemi|" + "top",
           selectedOptionSoru = "2 Soru|" + 2,
           selectedOptionAlarm = "Varsayılan Zil Sesi|"+0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_options);

        InitializingComponents();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LoadLastSettings();

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
    void OpenTheStartTimeOptions(View view)
    {
        dialogOption = new Dialog(this);
        dialogOption.setContentView(R.layout.alarm_baslangic_zamani_dialog);
        RadioButton rdDakikaOnce10 = (RadioButton) dialogOption.findViewById(R.id.dakikaOnce10);
        RadioButton rdDakikaOnce30 = (RadioButton) dialogOption.findViewById(R.id.dakikaOnce30);
        RadioButton rdDakikaOnce45 = (RadioButton) dialogOption.findViewById(R.id.dakikaOnce45);
        RadioButton rdDakikaOnce60 = (RadioButton) dialogOption.findViewById(R.id.dakikaOnce60);
        RadioButton rdDakikaOnce90 = (RadioButton) dialogOption.findViewById(R.id.dakikaOnce90);
        RadioButton rdDakikaOnce120 = (RadioButton) dialogOption.findViewById(R.id.dakikaOnce120);
        RadioButton rdDakikaOnce135 = (RadioButton) dialogOption.findViewById(R.id.dakikaOnce135);
        RadioButton rdDakikaOnce180 = (RadioButton) dialogOption.findViewById(R.id.dakikaOnce180);

        if (txtBaslangicZamani.getText().toString().equals(rdDakikaOnce10.getText().toString()))
            rdDakikaOnce10.setChecked(true);
        else if (txtBaslangicZamani.getText().toString().equals(rdDakikaOnce30.getText().toString()))
            rdDakikaOnce30.setChecked(true);
        else if (txtBaslangicZamani.getText().toString().equals(rdDakikaOnce45.getText().toString()))
            rdDakikaOnce45.setChecked(true);
        else if (txtBaslangicZamani.getText().toString().equals(rdDakikaOnce60.getText().toString()))
            rdDakikaOnce60.setChecked(true);
        else if (txtBaslangicZamani.getText().toString().equals(rdDakikaOnce90.getText().toString()))
            rdDakikaOnce90.setChecked(true);
        else if (txtBaslangicZamani.getText().toString().equals(rdDakikaOnce120.getText().toString()))
            rdDakikaOnce120.setChecked(true);
        else if (txtBaslangicZamani.getText().toString().equals(rdDakikaOnce135.getText().toString()))
            rdDakikaOnce135.setChecked(true);
        else
            rdDakikaOnce180.setChecked(true);
        dialogOption.show();
    }
    void OpenTheDegreeOfDifficultyOptions(View view)
    {
        dialogOption = new Dialog(this);
        dialogOption.setContentView(R.layout.zorluk_derecesi_dialog);
        RadioButton rdTop = (RadioButton) dialogOption.findViewById(R.id.rdTop);
        RadioButton rdCarp = (RadioButton) dialogOption.findViewById(R.id.rdCarp);
        RadioButton rdToplaVeCarp = (RadioButton) dialogOption.findViewById(R.id.rdTopVeCarp);

        if (txtZorlukDerecesi.getText().toString().equals(rdTop.getText().toString()))
            rdTop.setChecked(true);
        else if (txtZorlukDerecesi.getText().toString().equals(rdCarp.getText().toString()))
            rdCarp.setChecked(true);
        else
            rdToplaVeCarp.setChecked(true);
        dialogOption.show();
    }
    void OpenTheQuestionCountOptions(View view)
    {
        dialogOption = new Dialog(this);
        dialogOption.setContentView(R.layout.soru_sayisi_dialog);
        RadioButton rdSoru1 = (RadioButton) dialogOption.findViewById(R.id.rdSoru1);
        RadioButton rdSoru2 = (RadioButton) dialogOption.findViewById(R.id.rdSoru2);
        RadioButton rdSoru3 = (RadioButton) dialogOption.findViewById(R.id.rdSoru3);
        RadioButton rdSoru4 = (RadioButton) dialogOption.findViewById(R.id.rdSoru4);
        RadioButton rdSoru5 = (RadioButton) dialogOption.findViewById(R.id.rdSoru5);

        if (txtSoruSayisi.getText().toString().equals(rdSoru1.getText().toString()))
            rdSoru1.setChecked(true);
        else if (txtSoruSayisi.getText().toString().equals(rdSoru2.getText().toString()))
            rdSoru2.setChecked(true);
        else if (txtSoruSayisi.getText().toString().equals(rdSoru3.getText().toString()))
            rdSoru3.setChecked(true);
        else if (txtSoruSayisi.getText().toString().equals(rdSoru4.getText().toString()))
            rdSoru4.setChecked(true);
        else rdSoru5.setChecked(true);
        dialogOption.show();
    }
    void OpenTheAlarmSoundOptions(final View view)
    {
        try {
            dialogOption = new Dialog(this);
            dialogOption.setContentView(R.layout.alarm_sesi_dialog);
            RecyclerView recyclerView = dialogOption.findViewById(R.id.recycler_alarm);
            final RecyclerView.Adapter recAdapter;
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(dialogOption.getContext())); //recycler view yapılandırılmaları

            int selectedPosition=-1;
            String selectedAlarmName=txtAlarmSesi.getText().toString();
            List<Uri> alarmUriList = new ArrayList<Uri>();
            List<String> alarmNameList = new ArrayList<String>();
            RingtoneManager manager = new RingtoneManager(dialogOption.getContext());
            manager.setType(RingtoneManager.TYPE_ALL);
            Cursor cursor = manager.getCursor();
            cursor.moveToFirst();
            do {
                alarmNameList.add(cursor.getString(1));
                if(selectedAlarmName.equals(cursor.getString(1)))
                    selectedPosition=cursor.getPosition();
                alarmUriList.add(manager.getRingtoneUri(cursor.getPosition()));
            } while (cursor.moveToNext());

            recAdapter = new AlarmAdapter(dialogOption.getContext(), alarmNameList, alarmUriList, selectedPosition);
            recyclerView.setAdapter(recAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(false);
            Button btnCancel = dialogOption.findViewById(R.id.btn_vazgec);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogOption.dismiss();
                }
            });
            if(recAdapter.getItemCount()>0)
            dialogOption.show();
            else  Toast.makeText(view.getContext(),"Alarm sesi bulunamadı.",Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(view.getContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    void SelectStartAlarmTimeOption(View view){
        RadioButton rd = (RadioButton) view;
        txtBaslangicZamani.setText(rd.getText().toString());
        int time=180;
        switch (view.getId())
        {
            case R.id.dakikaOnce10:
                time=10;
                break;
            case R.id.dakikaOnce30:
                time=30;
                break;
            case R.id.dakikaOnce45:
                time=45;
                break;
            case R.id.dakikaOnce60:
                time=60;
                break;
            case R.id.dakikaOnce90:
                time=90;
                break;
            case R.id.dakikaOnce120:
                time=120;
                break;
            case R.id.dakikaOnce135:
                time=135;
                break;
        }
        selectedOptionBaslangic = rd.getText().toString() + "|" + time;
        dialogOption.dismiss();
    }
    void SelectDegreeOfDifficultyOption(View view){
        RadioButton rd = (RadioButton) view;
        txtZorlukDerecesi.setText(rd.getText().toString());
        String type = "top";
        switch (view.getId())
        {
            case R.id.rdCarp:
                type="carp";
                break;
            case R.id.rdTopVeCarp:
                type="topVeCarp";
                break;
        }
        selectedOptionZorluk = rd.getText().toString() + "|" + type;
        dialogOption.dismiss();
    }
    void SelectQuestionCountOption(View view){
        RadioButton rd = (RadioButton) view;
        txtSoruSayisi.setText(rd.getText().toString());
        int count = 5;
        switch (view.getId())
        {
            case R.id.rdSoru1:
                count=1;
                break;
            case R.id.rdSoru2:
                count=2;
                break;
            case R.id.rdSoru3:
                count=3;
                break;
            case R.id.rdSoru4:
                count=4;
                break;
        }
        selectedOptionSoru = rd.getText().toString() + "|" + count;
        dialogOption.dismiss();
    }

}
package com.mclk.devamsizliktakibi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;



public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtDonemBaslangic,
            txtDonemBitis,
            txtVize,
            txtFinal,
            txtSorguZamani;
    Button btnGuncelle;
    DatePickerDialog datePickerDialog;
    Dialog dialogOption;
    String selectedOption = "Dersin Bittiği An|" + 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        InitializingComponents();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LoadLastSettings();

    }

    void openTimePickerDialog(final View view) {
        if (view.getId() == R.id.etiket_donem_bas_tarihi) {
            {
                txtDonemBaslangic.callOnClick();
                return;
            }
        } else if (view.getId() == R.id.etiket_donem_bitis_tarihi) {
            txtDonemBitis.callOnClick();
            return;
        } else if (view.getId() == R.id.etiket_vize) {
            txtVize.callOnClick();
            return;
        } else if (view.getId() == R.id.etiket_final) {
            txtFinal.callOnClick();
            return;
        }

        final TextView txtDate = (TextView) view;


        String dateString = txtDate.getText().toString();
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(MainActivity.dateFormat.parse(dateString));
        } catch (Exception ex) {

        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                txtDate.setText(String.format("%02d", day) + "." + String.format("%02d", (month + 1)) + "." + String.format("%04d", year));

            }
        }, year, month, day);

        datePickerDialog.show();
    }

    void setDateTxt(TextView txtDate) {

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(MainActivity.dateFormat.parse(MainActivity.settingValues.getString(Integer.toString(txtDate.getId()), MainActivity.dateFormat.format(calendar.getTime()))));
        } catch (Exception ex) {

        }
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        txtDate.setText(String.format("%02d", dayOfMonth) + "." + String.format("%02d", month + 1) + "." + String.format("%04d", year));
    }

    void LoadLastSettings() {
        setDateTxt(txtDonemBaslangic);
        setDateTxt(txtDonemBitis);
        setDateTxt(txtFinal);
        setDateTxt(txtVize);
        String idLiStr = MainActivity.settingValues.getString(Integer.toString(txtSorguZamani.getId()), selectedOption);
        txtSorguZamani.setText(idLiStr.substring(0, idLiStr.indexOf('|')));
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle("Ayarlar");
    }

    void InitializingComponents() {
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        this.txtVize = (TextView) findViewById(R.id.txtVizeTarihi);
        this.txtFinal = (TextView) findViewById(R.id.txtFinalTarihi);
        this.txtDonemBaslangic = (TextView) findViewById(R.id.txt_donem_bas_tarihi);
        this.txtDonemBitis = (TextView) findViewById(R.id.txt_donem_bitis_tarihi);
        this.txtSorguZamani = (TextView) findViewById(R.id.txt_sorgu_zamani);
        this.btnGuncelle = (Button) findViewById(R.id.btn_ayarlar_guncelle);
    }

    void BtnGuncelleOnClick(View view) {
        if (view.getId() == R.id.btn_ayarlar_guncelle)
            if (CheckError()) {
                try {
                    MainActivity.settingValuesEditor.putString(Integer.toString(txtDonemBaslangic.getId()), txtDonemBaslangic.getText().toString());
                    MainActivity.settingValuesEditor.putString(Integer.toString(txtDonemBitis.getId()), txtDonemBitis.getText().toString());
                    MainActivity.settingValuesEditor.putString(Integer.toString(txtFinal.getId()), txtFinal.getText().toString());
                    MainActivity.settingValuesEditor.putString(Integer.toString(txtVize.getId()), txtVize.getText().toString());
                    MainActivity.settingValuesEditor.putString(Integer.toString(txtSorguZamani.getId()), selectedOption);
                    MainActivity.settingValuesEditor.apply();
                    Toast.makeText(this, "Veriler güncellendi.", Toast.LENGTH_SHORT).show();
                    this.finish();
                } catch (Exception ex) {
                }
            }
    }

    void OpenOptions(View view) {
        if (view.getId() == R.id.etiket_sorgu_zamani) {
            txtSorguZamani.callOnClick();
            return;
        }
        dialogOption = new Dialog(this);
        dialogOption.setContentView(R.layout.sorgu_zamanlari_dialog);
        RadioButton rdDersAni = (RadioButton) dialogOption.findViewById(R.id.rdDersAni0);
        RadioButton rdSonra10 = (RadioButton) dialogOption.findViewById(R.id.rdSonra10);
        RadioButton rdSonra30 = (RadioButton) dialogOption.findViewById(R.id.rdSonra30);
        RadioButton rdSonra60 = (RadioButton) dialogOption.findViewById(R.id.rdSonra60);

        if (txtSorguZamani.getText().toString().equals(rdDersAni.getText().toString()))
            rdDersAni.setChecked(true);
        else if (txtSorguZamani.getText().toString().equals(rdSonra10.getText().toString()))
            rdSonra10.setChecked(true);
        else if (txtSorguZamani.getText().toString().equals(rdSonra30.getText().toString()))
            rdSonra30.setChecked(true);
        else
            rdSonra60.setChecked(true);

        dialogOption.show();
    }

    void SelectOption(View view) {
        RadioButton rd = (RadioButton) view;
        txtSorguZamani.setText(rd.getText().toString());
        int time=0;
        switch (view.getId())
        {
            case R.id.rdSonra10:
                time=10;
                break;
            case R.id.rdSonra30:
                time=30;
                break;
            case R.id.rdSonra60:
                time=60;
                break;
        }

        selectedOption = rd.getText().toString() + "|" + time;
        dialogOption.dismiss();
    }

    boolean CheckError() {

        try {

            Calendar donemBaslangici = Calendar.getInstance();
            donemBaslangici.setTime(MainActivity.dateFormat.parse(txtDonemBaslangic.getText().toString()));

            Calendar donemBitisi = Calendar.getInstance();
            donemBitisi.setTime(MainActivity.dateFormat.parse(txtDonemBitis.getText().toString()));

            Calendar vizeTarihi = Calendar.getInstance();
            vizeTarihi.setTime(MainActivity.dateFormat.parse(txtVize.getText().toString()));

            Calendar finalTarihi = Calendar.getInstance();
            finalTarihi.setTime(MainActivity.dateFormat.parse(txtFinal.getText().toString()));


            Calendar nowTime = Calendar.getInstance();
            nowTime.set(Calendar.MILLISECOND, 0);
            nowTime.set(Calendar.SECOND, 0);
            nowTime.set(Calendar.MINUTE, 0);
            nowTime.set(Calendar.HOUR, 0);

            if (finalTarihi.getTime().getTime() < System.currentTimeMillis()) {
                Toast.makeText(this, "Final tarihiniz geçmiş bir tarihe ayarlanamaz.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (finalTarihi.getTime().getTime() < vizeTarihi.getTime().getTime()) {
                Toast.makeText(this, "Final tarihiniz vize tarihinden önce olamaz.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (finalTarihi.get(Calendar.YEAR) - vizeTarihi.get(Calendar.YEAR) > 1) {
                Toast.makeText(this, "Final tarihiniz ile vize tarihiniz arasında en fazla 1 yıl olabilir.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (vizeTarihi.getTime().getTime() < donemBaslangici.getTime().getTime()) {
                Toast.makeText(this, "Vize tarihiniz dönemin başlangıç tarihinden önce olamaz.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (finalTarihi.getTime().getTime() < donemBitisi.getTime().getTime()) {
                Toast.makeText(this, "Final tarihiniz dönemin bitiş tarihinden önce olamaz.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (donemBitisi.get(Calendar.YEAR) - donemBaslangici.get(Calendar.YEAR) > 1) {
                Toast.makeText(this, "Bir dönem bu kadar uzun olamaz. Lütfen geçerli bir aralık girin.", Toast.LENGTH_SHORT).show();
                return true;
            }

        } catch (Exception ex) {
            Toast.makeText(this, "Tarih bilgisi işlnemedi. Lütfen verilerinizi kontrol edin.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;


    }
}

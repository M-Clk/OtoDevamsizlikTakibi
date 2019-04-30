package com.mclk.devamsizliktakibi;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InitializingComponents();
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
        } else if (view.getId() == R.id.etiket_sorgu_zamani) {
            txtSorguZamani.callOnClick();
            return;
        }

        final TextView txtDate = (TextView) view;


        String dateString = txtDate.getText().toString();
        Calendar calendar=Calendar.getInstance();

        try {
           calendar.setTime(MainActivity.dateFormat.parse(dateString));
        }catch (Exception ex)
        {

        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Toast.makeText(this,calendar.getTime().toString()+"",Toast.LENGTH_LONG).show();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                txtDate.setText(String.format("%02d", day) + "." + String.format("%02d", (month+1)) + "." + String.format("%04d", year));

            }
        }, year, month, day);

        datePickerDialog.show();
    }

    void setDateTxt(TextView txtDate, String valueName) {

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(MainActivity.dateFormat.parse(MainActivity.settingValues.getString(valueName, MainActivity.dateFormat.format(calendar.getTime()))));
        }
        catch (Exception ex)
        {

        }
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        txtDate.setText(String.format("%02d", dayOfMonth) + "." + String.format("%02d", month+1) + "." + String.format("%04d", year));
    }

    void LoadLastSettings() {
        setDateTxt(txtDonemBaslangic, "donemBaslangicTarihi");
        setDateTxt(txtDonemBitis, "donemBitisTarihi");
        setDateTxt(txtFinal, "finalTarihi");
        setDateTxt(txtVize, "vizeTarihi");
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
    void BtnGuncelleOnClick(View view)
    {

    }

}

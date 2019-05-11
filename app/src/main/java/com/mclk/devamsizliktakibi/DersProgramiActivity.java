package com.mclk.devamsizliktakibi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DersProgramiActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences settingValues;
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;
    Dialog dialogAddTime;
    public static String selectedDersName;
    public static int selectedId = -1;
    TextView
            txtBaslangicSaati,
            txtBitisiSaati;
    int
            basSaati = -1,
            basDakika,
            bitSaati = -1,
            bitDakika;
    int lessonCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_programi);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle((CharSequence) "Ders Programı");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.settingValues = getSharedPreferences("com.mclk.devamsizliktakibi", 0);
        try {
            this.viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(this.viewPager);
            this.tabLayout = (TabLayout) findViewById(R.id.tabs);
            this.tabLayout.setupWithViewPager(this.viewPager);
            this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tabLayout));
            this.tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(this.viewPager));
            ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(this);
            dbVeriIslemMerkezi dbVeriIslemMerkezi = new dbVeriIslemMerkezi(this);
            lessonCount=(dbVeriIslemMerkezi.dersListele()).size();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        this.adapter = new ViewPagerAdapter(getSupportFragmentManager());
        this.adapter.addFrag(new Pazartesi(), "PAZARTESİ");
        this.adapter.addFrag(new Sali(), "SALI");
        this.adapter.addFrag(new Carsamba(), "ÇARŞAMBA");
        this.adapter.addFrag(new Persembe(), "PERŞEMBE");
        this.adapter.addFrag(new Cuma(), "CUMA");
        viewPager.setAdapter(this.adapter);

        Calendar cldr = Calendar.getInstance();
        if (cldr.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cldr.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
            viewPager.setCurrentItem(cldr.get(Calendar.DAY_OF_WEEK) - 2, true);
    }

    void OpenAddingTimeDialog(View view) {
        if(lessonCount==0)
        {
            Toast.makeText(view.getContext(),"Ders programına eklenebilecek bir ders bulunamadı. Öncelikle soldaki menüden Dersler'e giderek ders ekleyin.",Toast.LENGTH_LONG).show();
            return;
        }
        dialogAddTime = new Dialog(this);
        dialogAddTime.setContentView(R.layout.ders_programi_ekle);

        TextView txtDersAdi = (TextView) dialogAddTime.findViewById(R.id.txt_ders_adi);
        txtDersAdi.setOnClickListener(this);
        ((ImageView) dialogAddTime.findViewById(R.id.image_baslangic)).setOnClickListener(this);
        ((ImageView) dialogAddTime.findViewById(R.id.image_bitis)).setOnClickListener(this);
        txtBaslangicSaati = (TextView) dialogAddTime.findViewById(R.id.txt_baslangic);
        txtBaslangicSaati.setOnClickListener(this);
        txtBitisiSaati = (TextView) dialogAddTime.findViewById(R.id.txt_bitis);
        txtBitisiSaati.setOnClickListener(this);
        ((Button) dialogAddTime.findViewById(R.id.btn_ders_programi_ekle)).setOnClickListener(this);
        dialogAddTime.show();
        dialogAddTime.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                RefreshVariables();
            }
        });
    }

    void OpenTimePickerDialog(View view, final TextView txtTime, final boolean isStartTime) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if (isStartTime) {
                    basSaati = hour;
                    basDakika = minute;
                } else {
                    bitSaati = hour;
                    bitDakika = minute;
                }
                txtTime.setText((hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute));
            }
        }, isStartTime ? (basSaati == -1 ? 0 : basSaati) : (bitSaati == -1 ? 0 : bitSaati), !isStartTime ? (basSaati == -1 ? 0 : basDakika) : (bitSaati == -1 ? 0 : bitDakika), true);

        timePickerDialog.setIcon(isStartTime ? R.drawable.ic_timer_black_24dp : R.drawable.ic_timer_off_black_24dp);
        timePickerDialog.show();
    }

    void OpenLessonNames(final View view) {
        try {
            dbVeriIslemMerkezi veriIslemMerkezi = new dbVeriIslemMerkezi(view.getContext());
            List<tblDers> dersList = veriIslemMerkezi.dersListele();
            List<String> dersAdiList = new ArrayList<String>();
            List<Integer> dersIdList = new ArrayList<Integer>();
            for (int i = 0; i < dersList.size(); i++) {
                dersAdiList.add(dersList.get(i).getAdi());
                dersIdList.add(dersList.get(i).getId());
            }
            Dialog dialogNameSelect = new Dialog(view.getContext());
            dialogNameSelect.setContentView(R.layout.select_options_dialog);
            ((TextView) dialogNameSelect.findViewById(R.id.txtBaslik)).setText("Dersi seçin.");
            RecyclerView recyclerView = (RecyclerView) dialogNameSelect.findViewById(R.id.recycler_options);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(dialogNameSelect.getContext()));

            RecyclerView.Adapter recAdapter = new OptionsAdapter(dersAdiList, dersIdList, dialogNameSelect.getContext(), dialogNameSelect);
            recyclerView.setAdapter(recAdapter);
            dialogNameSelect.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    ((TextView) view).setText(selectedDersName);
                }
            });
            dialogNameSelect.show();
        } catch (Exception ex) {
            Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void AddTime(final View view) {
        if (ErrorCheck(view.getContext())) {
            dbVeriIslemMerkezi dbIslemMerkezi = new dbVeriIslemMerkezi(view.getContext());
            List<tblDersProgrami> dersProgramiList = dbIslemMerkezi.dersProgramiListele(viewPager.getCurrentItem());
            ZamanMerkezi zamanMerkezi = new ZamanMerkezi();

            int startTotlMins = basSaati * 60 + basDakika;
            int finishTotlMins = bitSaati * 60 + bitDakika;
            for (int i = 0; i < dersProgramiList.size(); i++) {
                int tempStartTotlMins = dersProgramiList.get(i).getBasSaati();
                int tempFinishTotlMins = dersProgramiList.get(i).getBitSaati();

                int tempStartHour = zamanMerkezi.IntToHours(dersProgramiList.get(i).getBasSaati());
                int tempStartMinute = zamanMerkezi.IntToMinutes(dersProgramiList.get(i).getBasSaati());
                int tempFinishHour = zamanMerkezi.IntToHours(dersProgramiList.get(i).getBitSaati());
                int tempFinishMinute = zamanMerkezi.IntToHours(dersProgramiList.get(i).getBitSaati());

                if (((startTotlMins < tempFinishTotlMins) && (startTotlMins > tempStartTotlMins)) ||
                        ((tempStartTotlMins < finishTotlMins) && (tempStartTotlMins > startTotlMins)) ||
                        ((startTotlMins < tempFinishHour) && (finishTotlMins > tempFinishHour)) ||
                        ((startTotlMins > tempFinishHour) && (finishTotlMins < tempFinishHour))) {
                    String tempTimeString = String.format("%02d", tempStartHour) + ":" + String.format("%02d", tempStartMinute) + "-" + String.format("%02d", tempFinishHour) + ":" + String.format("%02d", tempFinishMinute);
                    Toast.makeText(view.getContext(), "Bu saat aralığında,\n " + dersProgramiList.get(i).getDersAdi() + "(" + tempTimeString + ") \ndersiniz bulunmaktadır. Aralığı değiştirin.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            tblDersProgrami eklenecekDersPrg = new tblDersProgrami(0, selectedId, selectedDersName, viewPager.getCurrentItem(), startTotlMins, finishTotlMins);
            if (dbIslemMerkezi.dersProgramiEkle(eklenecekDersPrg) > 0) {
                CreateNotification();
                setupViewPager(viewPager);
                viewPager.setCurrentItem(eklenecekDersPrg.getGunId());
            }
            RefreshVariables();
            dialogAddTime.dismiss();
        }
    }
    @Override
    public void onClick(final View view) {
        if (view.getId() == R.id.fab)
            OpenAddingTimeDialog(view);
        else if (view.getId() == R.id.txt_baslangic || view.getId() == R.id.image_baslangic)
            OpenTimePickerDialog(view, txtBaslangicSaati, true);
        else if (view.getId() == R.id.txt_bitis || view.getId() == R.id.image_bitis)
            OpenTimePickerDialog(view, txtBitisiSaati, false);
        else if (view.getId() == R.id.txt_ders_adi)
            OpenLessonNames(view);
        else if (view.getId() == R.id.btn_ders_programi_ekle)
            AddTime(view);
    }

    void CreateNotification() {

    }

    void RefreshVariables() {
        basSaati = -1;
        bitSaati = -1;
        selectedId = -1;
        selectedDersName = "";
    }

    boolean ErrorCheck(Context context) {
        if (selectedId == -1) {
            Toast.makeText(context, "Bir ders seçin.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (basSaati == -1) {
            Toast.makeText(context, "Dersin başlangıç saatini girin.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (bitSaati == -1) {
            Toast.makeText(context, "Dersin bitiş saatini girin.", Toast.LENGTH_LONG).show();
            return false;
        }

        if ((bitSaati - basSaati) < 0) {
            Toast.makeText(context, "Ders başlangıç ve bitiş saatlerinizi kontrol edin. Bitiş saati başlangıç saatinden önce olamaz.", Toast.LENGTH_LONG).show();
            return false;
        }
        if ((bitSaati - basSaati) == 0 && (bitDakika - basDakika) < 10) {
            Toast.makeText(context, "Ders başlangıç ve bitiş saatlerinizi kontrol edin. Bitiş tarihi başlangıç tarihinden en az 10 dakika sonra olmalıdır.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}

package com.mclk.devamsizliktakibi;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;

import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class DersProgramiActivity extends AppCompatActivity {

    SharedPreferences settingValues;
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;
    DatePickerDialog datePickerDialog;


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
            ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar calendar = Calendar.getInstance();
                    int mDay= calendar.DAY_OF_MONTH;
                    int mYear = calendar.YEAR;
                    int mMonth = calendar.MONTH;
datePickerDialog = new DatePickerDialog(DersProgramiActivity.this, new DatePickerDialog.OnDateSetListener() {
    @Override
    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
//toolbar.setTitle(mDay+"/"+mMonth+"/"+mYear);
    }
},mYear,mMonth,mDay);
datePickerDialog.show();
                }
            });
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

        if(cldr.DAY_OF_WEEK!=Calendar.SATURDAY && cldr.DAY_OF_WEEK!=Calendar.SUNDAY)
        viewPager.setCurrentItem(cldr.DAY_OF_WEEK-2);

    }
}

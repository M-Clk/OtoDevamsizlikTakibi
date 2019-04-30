package com.mclk.devamsizliktakibi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

public class AlarmOptionsActivity extends AppCompatActivity {
    Toolbar toolbar;
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
    }

}

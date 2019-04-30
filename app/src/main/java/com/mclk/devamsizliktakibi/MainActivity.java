package com.mclk.devamsizliktakibi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {


    dbVeriIslemMerkezi dbveriIslemMerkezi;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Adapter recAdapter;
    RecyclerView recyclerView;
    public static SharedPreferences settingValues;
    public static SimpleDateFormat dateFormat;
    List<tblDers> tblDersList;
    Toolbar toolbar;
    TextView txtBilgi;
    TextView txtFinal;
    TextView txtVize;


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    void InitializingComponents() {
        this.txtBilgi = (TextView) findViewById(R.id.txt_bilgi);
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.navigationView = (NavigationView) findViewById(R.id.navigation_view);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.txtVize = (TextView) findViewById(R.id.txt_vizeye_kalan);
        this.txtFinal = (TextView) findViewById(R.id.txt_finale_kalan);
        this.settingValues = getSharedPreferences("com.mclk.devamsizliktakibi", 0);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.navigation_drawer);

        InitializingComponents();
        setSupportActionBar(toolbar);


        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        this.settingValues = getSharedPreferences("com.mclk.devamsizliktakibi", MODE_PRIVATE);
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.dbveriIslemMerkezi = new dbVeriIslemMerkezi(this);
        this.tblDersList = this.dbveriIslemMerkezi.dersListele();

        //this.dbveriIslemMerkezi.bildirimVeAlarmYukle(this);
        this.recAdapter = new MyAdapter(this.tblDersList, this);
        this.recyclerView.setAdapter(this.recAdapter);

        if (this.recAdapter.getItemCount() == 0) {
            this.txtBilgi.setVisibility(View.VISIBLE);
        }

        int vizeInt = this.settingValues.getInt("vizeTarihi", 0);
        int finalInt = this.settingValues.getInt("finalTarihi", 0);
        if (SinavaKalan(vizeInt) < 0) {
            this.txtVize.setText("-");
        } else {
            this.txtVize.setText(Integer.toString(SinavaKalan(vizeInt)));
        }
        if (SinavaKalan(finalInt) < 0) {
            this.txtFinal.setText("-");
        } else {
            this.txtFinal.setText(Integer.toString(SinavaKalan(finalInt)));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.alarm_options:
                startActivity(new Intent(this, AlarmOptionsActivity.class));
                this.drawerLayout.closeDrawer(navigationView);
                break;
            case R.id.dersler:
                startActivity(new Intent(this, DerslerActivity.class));
                this.drawerLayout.closeDrawer(this.navigationView);
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                this.drawerLayout.closeDrawer(this.navigationView);
                break;
            case R.id.syllabus:
                startActivity(new Intent(this, DersProgramiActivity.class));
                this.drawerLayout.closeDrawer(this.navigationView);
                break;
            case R.id.tatiller:
                startActivity(new Intent(this, TatillerActivity.class));
                this.drawerLayout.closeDrawer(this.navigationView);
                break;
            default:
                break;
        }
        return false;
    }

    int SinavaKalan(int i) {
        try {
            return (i - ((int) (System.currentTimeMillis() / 1000))) / 604800;
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            return 0;
        }
    }
}


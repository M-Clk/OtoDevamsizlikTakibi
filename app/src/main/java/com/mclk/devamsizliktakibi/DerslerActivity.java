package com.mclk.devamsizliktakibi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DerslerActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtBilgi;
    RecyclerView.Adapter recAdapter;
    RecyclerView recyclerView;
    dbVeriIslemMerkezi dbveriIslemMerkezi;
    List<tblDers> tblDersList;
    FloatingActionButton btnAddDers;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dersler);

        InitializingComponents();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setData();
    }
    void setData()
    {
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this)); //recycler view yapılandırılmaları
        dbveriIslemMerkezi = new dbVeriIslemMerkezi(this);
        tblDersList = dbveriIslemMerkezi.dersListele(); //veribaından veilerin çekilip listeye katarılması.
        recAdapter = new DersIslemAdapter(this, tblDersList); // Liste ve context gönderilerek adapter olışturlması.

        recyclerView.setAdapter(recAdapter); // RecyclerView adapter yardımı ile verilerin yazılması.

        if (this.recAdapter.getItemCount() == 0) { // Eğer adapter'da veriler yoksa txtBilgi textview'ı aktif olacakır.
            this.txtBilgi.setVisibility(View.VISIBLE);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    void InitializingComponents() {
        this.txtBilgi = (TextView) findViewById(R.id.txt_bilgi);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_ders);
        this.btnAddDers = (FloatingActionButton) findViewById(R.id.btn_ders_add);
        this.btnAddDers.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle((CharSequence) "Dersler");
    }

    @Override
    public void onClick(View view) {
        if (view == this.btnAddDers) {
            ShowEkleDialog(this);
        }
    }
    public void ShowEkleDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.ekle_ders_temp);
        final EditText txtDersAdiEkle = (EditText) dialog.findViewById(R.id.txt_ekle_ders_adi);
        Button btnEkle = (Button) dialog.findViewById(R.id.btn_ders_ekle);
        final Spinner spnKredi = (Spinner) dialog.findViewById(R.id.spn_kredi);
        final EditText txtDevamsizlik = (EditText) dialog.findViewById(R.id.txt_ekle_devamsizlik);
        final EditText txtDevamsizlikSiniri = (EditText) dialog.findViewById(R.id.txt_ekle_devamsizlik_siniri);
        final EditText txtKritikSinir = (EditText) dialog.findViewById(R.id.txt_ekle_kritik_sinir);
        final CheckBox chbSinir = (CheckBox) dialog.findViewById(R.id.cbSinir);

        dialog.show();
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(dialog.getContext(), R.layout.spinner_item, new ArrayList(Arrays.asList(new String[]{"Kredi", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"})));
        ((ArrayAdapter) spinnerAdapter).setDropDownViewResource(R.layout.spinner_item);
        spnKredi.setAdapter(spinnerAdapter);
        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String
                        dersAdi = txtDersAdiEkle.getText().toString();
                double
                        devamsizlik = Double.parseDouble(txtDevamsizlik.getText().toString()),
                        devSiniri = Double.parseDouble(txtDevamsizlikSiniri.getText().toString()),
                        kritikSinir = Double.parseDouble(txtKritikSinir.getText().toString());
                int kredi = Integer.parseInt(spnKredi.getSelectedItem().toString());

                boolean kritikSinirChecked = chbSinir.isChecked();
                if (eklemeHataKontrol(dersAdi, devamsizlik, devSiniri, kritikSinir, kredi, kritikSinirChecked)) {
                    if (!kritikSinirChecked) kritikSinir = -1;
                    tblDers eklenecekDers = new tblDers(dersAdi, kredi, devSiniri, devamsizlik, kritikSinir);
                    dbVeriIslemMerkezi veriM = new dbVeriIslemMerkezi(dialog.getContext());
                    if (veriM.dersEkle(eklenecekDers) == -1)
                    Toast.makeText(dialog.getContext(),"Ders eklenemedi. Verilerinizi kontrol edin. Tekrar deneyin.",Toast.LENGTH_LONG).show();
                    else
                    {
                        dialog.dismiss();
                        setData();
                    }

                }

            }
        });
    }
    public void addData(Context context, String titleText)
    {

    }
    public boolean eklemeHataKontrol(String dersAdi, double devamsizlik, double devSiniri, double kritikSinir, int kredi, boolean kritikSinirChecked) {
        if (kritikSinir >= 0 || kritikSinir==-1 ){
            if (devamsizlik >= 0) {
                if (devSiniri >= 0) {
                    if (devamsizlik >= devSiniri && kritikSinir <= devSiniri && kritikSinirChecked) {
                        Toast.makeText(this, "Devamsızlık sınırınızı aştıysanız kritik sınır belirleyemezsiniz.", Toast.LENGTH_SHORT).show();
                        return false;
                    } else if ( devSiniri >= 14) {
                        Toast.makeText(this, "Devamsızlık sınırı 13 haftayı geçemez.", Toast.LENGTH_SHORT).show();
                        return false;
                    } else if (dersAdi.length() < 6) {
                        Toast.makeText(this, "Ders adı en az 6 karakter olmalıdır.", Toast.LENGTH_SHORT).show();
                        return false;
                    } else if (kredi != 0) {
                        return true;
                    } else {
                        Toast.makeText(this, "Lütfen dersin kredisini seçin.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
        }
        Toast.makeText(this, "Lütfen negatif değerler girmeyin.", Toast.LENGTH_SHORT).show();
        return false;
    }

}

package com.mclk.devamsizliktakibi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class Carsamba extends Fragment {
    dbVeriIslemMerkezi dbveriIslemMerkezi;
    List<tblDersProgrami> dersProgramiList;
    RecyclerView recyclerView;
    public static TextView txtBilgi;
    View view;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        try {
            this.view = layoutInflater.inflate(R.layout.activity_carsamba, viewGroup, false);
            this.txtBilgi = (TextView) this.view.findViewById(R.id.txt_bilgi_carsamba);
            this.recyclerView = (RecyclerView) this.view.findViewById(R.id.recyclerViewCarsamba);
            this.recyclerView.hasFixedSize();
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this.view.getContext()));
            this.dbveriIslemMerkezi = new dbVeriIslemMerkezi(layoutInflater.getContext());
            this.dersProgramiList = this.dbveriIslemMerkezi.dersProgramiListele(2);
            RecyclerView.Adapter recAdapter = new DersAdapter(this.dersProgramiList, layoutInflater.getContext(), this.recyclerView,2);
            this.recyclerView.setAdapter(recAdapter);

            if (recAdapter.getItemCount() == 0) {

                this.txtBilgi.setVisibility(View.VISIBLE);
            }
            return this.view;
        } catch (Exception ex) {
            Toast.makeText(layoutInflater.getContext(), "Hata", Toast.LENGTH_LONG).show();
            return this.view;
        }
    }
}
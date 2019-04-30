package com.mclk.devamsizliktakibi;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class DersAdapter extends RecyclerView.Adapter<DersAdapter.ViewHolder>
{
    private Context context;
    dbVeriIslemMerkezi dbveriIslemMerkezi;
    private List<tblDersProgrami> dersProgramiList;
    RecyclerView recyclerView;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ImageButton btnSil;
        CardView dersProgrami;
        public TextView txtBasSaati;
        public TextView txtBitSaati;
        public TextView txtDersAdi;
        public TextView txtId;

        public ViewHolder(View view) {
            super(view);
            this.txtDersAdi = (TextView) view.findViewById(R.id.txt_ders_adi);
            this.txtBasSaati = (TextView) view.findViewById(R.id.txt_bas_saati);
            this.txtBitSaati = (TextView) view.findViewById(R.id.txt_bit_saati);
            this.txtId = (TextView) view.findViewById(R.id.txt_id);
            this.btnSil = (ImageButton) view.findViewById(R.id.btn_program_sil);
            this.dersProgrami = (CardView) view.findViewById(R.id.crd_ders_programi);
        }
    }

    public DersAdapter(List<tblDersProgrami> list, Context context, RecyclerView recyclerView) {
        this.dersProgramiList = list;
        this.context = context;
        this.recyclerView = recyclerView;
    }
}

package com.mclk.devamsizliktakibi;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class DersAdapter extends RecyclerView.Adapter<DersAdapter.ViewHolder>
{
    private Context context;
    dbVeriIslemMerkezi dbveriIslemMerkezi;
    private List<tblDersProgrami> dersProgramiList;
    RecyclerView recyclerView;
    LayoutInflater inflater;
    ZamanMerkezi zamanMerkezi;

    public DersAdapter(List<tblDersProgrami> list, Context context, RecyclerView recyclerView) {
        this.dersProgramiList = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.recyclerView = recyclerView;
        zamanMerkezi = new ZamanMerkezi();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = inflater.inflate(R.layout.ders_programi_elemani, parent, false); //cardView tanıtılır.
        return new ViewHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.SetData(dersProgramiList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return dersProgramiList.size();
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

        void SetData(tblDersProgrami selectedDersPrg, int position) {
            try {
                txtId.setText(Integer.toString(selectedDersPrg.getDersId()));
                txtDersAdi.setText(selectedDersPrg.getDersAdi());

                txtBasSaati.setText(GetTimeStr(selectedDersPrg.getBasSaati()));
                txtBitSaati.setText(GetTimeStr(selectedDersPrg.getBitSaati()));
            }
            catch (Exception ex)
            {
                Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();
            }

        }
        String GetTimeStr(int intTime)
        {
            String timeStr;
            int hour=zamanMerkezi.IntToDate(intTime).getHours();
            int minute=zamanMerkezi.IntToDate(intTime).getMinutes();
            timeStr=(hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
            return  timeStr;
        }
    }


}

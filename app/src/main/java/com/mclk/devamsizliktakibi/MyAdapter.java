package com.mclk.devamsizliktakibi;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.List;

public class MyAdapter  extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    LayoutInflater inflater;
    private List<tblDers> dersList;



    public MyAdapter(List<tblDers> list, Context context) {
        this.dersList = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {
        View v = inflater.inflate(R.layout.list_item, viewGroup, false);
        return new MyAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(dersList.get(i),i);
    }

    public int getItemCount() {

        return this.dersList.size();
    }

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ProgressBar pbLvl;
        public TextView txtDersAdi;
        public TextView txtDersKredisi;
        public TextView txtPbDurum;


        public ViewHolder(View view) {
            super(view);
            this.txtDersAdi = (TextView) view.findViewById(R.id.txt_ders_adi);
            this.txtDersKredisi = (TextView) view.findViewById(R.id.txt_kredi);
            this.txtPbDurum = (TextView) view.findViewById(R.id.txt_pb_durum);
            this.pbLvl = (ProgressBar) view.findViewById(R.id.pb_lvl);

        }
        public void setData(tblDers selectedDers, int position)
        {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            txtDersKredisi.setText(Integer.toString(selectedDers.getKredi()));
            txtDersAdi.setText(selectedDers.getAdi());
            double devamsizlik = (selectedDers.getDevamsizlik() * 100.0d) / selectedDers.getDevSiniri();
            int intDevamsizlik = (int) devamsizlik;
            if (intDevamsizlik > 0) {
                pbLvl.setProgress(intDevamsizlik);
            } else {
                pbLvl.setVisibility(View.INVISIBLE);
            }
            String devDurumuTxt = "Devamsızlık Durumu: "+ decimalFormat.format(selectedDers.getDevamsizlik())+ "/"+
            decimalFormat.format(selectedDers.getDevSiniri());
            if (devamsizlik > 100) {
                devDurumuTxt = devamsizlik + " (Kaldınız!)";
                txtPbDurum.setTextColor(Color.RED);
            } else {
              int i3 = (devamsizlik > 0 ? 1 : (devamsizlik == 0.0d ? 0 : -1));
            }
            txtPbDurum.setText(devDurumuTxt);
        }
    }

}

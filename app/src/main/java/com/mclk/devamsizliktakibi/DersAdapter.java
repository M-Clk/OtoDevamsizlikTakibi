package com.mclk.devamsizliktakibi;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class DersAdapter extends RecyclerView.Adapter<DersAdapter.ViewHolder>
{
    private Context context;
    dbVeriIslemMerkezi dbveriIslemMerkezi;
    private List<tblDersProgrami> dersProgramiList;
    RecyclerView recyclerView;
    LayoutInflater inflater;
    ZamanMerkezi zamanMerkezi;
    int gunId;

    public DersAdapter(List<tblDersProgrami> list, Context context, RecyclerView recyclerView, int gunId) {
        this.dersProgramiList = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.recyclerView = recyclerView;
        zamanMerkezi = new ZamanMerkezi();
        this.gunId = gunId;
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

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageButton btnSil;
        CardView dersProgrami;
        public TextView txtBasSaati;
        public TextView txtBitSaati;
        public TextView txtDersAdi;
        public TextView txtId;
        int selectedPosition;


        public ViewHolder(View view) {
            super(view);
            this.txtDersAdi = (TextView) view.findViewById(R.id.txt_ders_adi);
            this.txtBasSaati = (TextView) view.findViewById(R.id.txt_bas_saati);
            this.txtBitSaati = (TextView) view.findViewById(R.id.txt_bit_saati);
            this.txtId = (TextView) view.findViewById(R.id.txt_id);
            this.btnSil = (ImageButton) view.findViewById(R.id.btn_program_sil);
            this.dersProgrami = (CardView) view.findViewById(R.id.crd_ders_programi);
            btnSil.setOnClickListener(this);
        }

        void SetData(tblDersProgrami selectedDersPrg, int position) {
            selectedPosition=position;
            try {
                txtId.setText(Integer.toString(selectedDersPrg.getId()));
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

        @Override
        public void onClick(final View view) {
            if(view.getId()==R.id.btn_program_sil)
            {
                AlertDialog.Builder ConfirmDialog = new AlertDialog.Builder(view.getContext());
                ConfirmDialog.setTitle("Kritik İşlem");
                ConfirmDialog.setMessage("Bir dersi ders programından silmeye çalışıyorsunuz. Devam etmek istiyorsanız TAMAM butonuna dokunmanız yeterli.");
                ConfirmDialog.setIcon(R.drawable.warning);
                ConfirmDialog.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbveriIslemMerkezi = new dbVeriIslemMerkezi(view.getContext());
                        if (dbveriIslemMerkezi.DersProgramiSil(txtId.getText().toString()) >0) {
                            AlarmManager alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(PendingIntent.getBroadcast(view.getContext(), Integer.valueOf(txtId.getText().toString()).intValue(), new Intent(view.getContext(), UyariDinleyici.class), 0));
                            alarmManager.cancel(PendingIntent.getBroadcast(view.getContext(), -Integer.valueOf(txtId.getText().toString()).intValue(), new Intent(view.getContext(), AlarmDinleyici.class), 0));
                            alarmManager.cancel(PendingIntent.getBroadcast(view.getContext(),1147483647-Integer.valueOf(txtId.getText().toString()).intValue(),new Intent(view.getContext(),MuteControlReceiver.class),0));
                            alarmManager.cancel(PendingIntent.getBroadcast(view.getContext(),-1147483647+Integer.valueOf(txtId.getText().toString()).intValue(),new Intent(view.getContext(),MuteControlReceiver.class),0));
                            deleteItem(selectedPosition);
                            if(dersProgramiList.size()==0)
                            {
                                switch (gunId)
                                {
                                    case 0:
                                        Pazartesi.txtBilgi.setVisibility(View.VISIBLE);
                                        break;
                                    case 1:
                                        Sali.txtBilgi.setVisibility(View.VISIBLE);
                                        break;
                                    case 2:
                                        Carsamba.txtBilgi.setVisibility(View.VISIBLE);
                                        break;
                                    case 3:
                                        Persembe.txtBilgi.setVisibility(View.VISIBLE);
                                        break;
                                    case 4:
                                        Cuma.txtBilgi.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                });
                ConfirmDialog.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                ConfirmDialog.show();
            }
        }
        private void deleteItem(int position) {
            dersProgramiList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, dersProgramiList.size());

        }
    }


}

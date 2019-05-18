package com.mclk.devamsizliktakibi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    public class DersIslemAdapter extends RecyclerView.Adapter<DersIslemAdapter.ViewHolder> {
        private Context context;
        private List<tblDers> dersList;
        LayoutInflater inflater;

        public DersIslemAdapter(Context context, List<tblDers> list) {
            this.dersList = list; //veri listesi alındı.
            inflater = LayoutInflater.from(context); //inflater bu context için çalışacak.
            this.context = context;
        }
        @Override
        @NonNull

        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View inflateView = inflater.inflate(R.layout.activity_ders, viewGroup, false); //cardView tanıtılır.
            return new ViewHolder(inflateView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setData(dersList.get(position), position);//Liste elemanı sırayla verilerle dolduruluyor.
        }

        @Override
        public int getItemCount() {
            return dersList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageButton btnSil;
            public CardView crdView;
            public ProgressBar pbLvl;
            public TextView txtDersAdi;
            public TextView txtDersKredisi;
            public TextView txtId;
            public TextView txtKritikSinir;
            public TextView txtPbDurum;
            tblDers selectedDers;

            public ViewHolder(View view) {
                super(view);
                this.txtDersAdi = (TextView) view.findViewById(R.id.txt_ders_adi);
                this.txtDersKredisi = (TextView) view.findViewById(R.id.txt_kredi);
                this.txtPbDurum = (TextView) view.findViewById(R.id.txt_pb_durum);
                this.pbLvl = (ProgressBar) view.findViewById(R.id.pb_lvl);
                this.txtKritikSinir = (TextView) view.findViewById(R.id.txt_kritik_sinir);
                this.crdView = (CardView) view.findViewById(R.id.crd_view);
                this.txtId = (TextView) view.findViewById(R.id.txt_id);
                this.btnSil = (ImageButton) view.findViewById(R.id.btn_sil);
                btnSil.setOnClickListener(this);
                crdView.setOnClickListener(this);
            }

            public void setData(tblDers selectedDers, int position) {
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                this.selectedDers = selectedDers;
                txtDersKredisi.setText(Integer.toString(selectedDers.getKredi()));
                txtDersAdi.setText(selectedDers.getAdi());
                double devamsizlik = (selectedDers.getDevamsizlik() * 100.0d) / selectedDers.getDevSiniri();
                int intDevamsizlik = (int) devamsizlik;
                if (intDevamsizlik > 0) {
                    pbLvl.setProgress(intDevamsizlik);
                } else {
                    pbLvl.setVisibility(View.INVISIBLE);
                }
                String devDurumuTxt = "Devamsızlık Durumu: " + decimalFormat.format(selectedDers.getDevamsizlik()) + "/" +
                        decimalFormat.format(selectedDers.getDevSiniri());
                if (devamsizlik > 100) {
                    devDurumuTxt +=" (Kaldınız!)";
                    txtPbDurum.setTextColor(Color.RED);
                }
                else txtPbDurum.setTextColor(txtKritikSinir.getTextColors().getDefaultColor());
                txtPbDurum.setText(devDurumuTxt);

                    txtKritikSinir.setText("Kritik Alarm Sınırı: " + decimalFormat.format(selectedDers.getKritikSinir()));
                    if(selectedDers.getKritikSinir()==-1)txtKritikSinir.setVisibility(View.INVISIBLE);
                    else txtKritikSinir.setVisibility(View.VISIBLE);

                txtId.setText(Integer.toString(selectedDers.getId()));
            }

            @Override
            public void onClick(final View view) {
                if (view == this.btnSil) {//sil butonu tıllanınca
                    AlertDialog.Builder ConfirmDialog = new AlertDialog.Builder(view.getContext());
                    ConfirmDialog.setTitle("Kritik İşlem");
                    ConfirmDialog.setMessage("Bir dersi silmeye çalışıyorsunuz. Devam etmek istiyorsanız TAMAM butonuna dokunmanız yeterli.");
                    ConfirmDialog.setIcon(R.drawable.warning);
                    ConfirmDialog.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbVeriIslemMerkezi vMerkezi = new dbVeriIslemMerkezi(context);
                            if (vMerkezi.dersSil(txtId.getText().toString()) > 0)
                                deleteItem(getLayoutPosition());//Elemanın index numarasını(position) göndererek silme işlemini başlatır.
                            else
                                Toast.makeText(view.getContext(), "Ders silinemedi. Bu dersin ders programında ekli olmadığından emin olun. Tekrar deneyin.", Toast.LENGTH_LONG).show();
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
                 else if (view == this.crdView)//Cardview tıklanınca
                {
                    ShowGuncelleDialog(view.getContext(), selectedDers);
                }
            }
            public void ShowGuncelleDialog(Context context, tblDers selectedDers) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.ekle_ders_temp);

                DecimalFormat decimalFormat = new DecimalFormat("#.##");

                final EditText txtDersAdiEkle = (EditText) dialog.findViewById(R.id.txt_ekle_ders_adi);
                Button btnGuncelle = (Button) dialog.findViewById(R.id.btn_ders_ekle);
                final Spinner spnKredi = (Spinner) dialog.findViewById(R.id.spn_kredi);
                final EditText txtDevamsizlik = (EditText) dialog.findViewById(R.id.txt_ekle_devamsizlik);
                final EditText txtDevamsizlikSiniri = (EditText) dialog.findViewById(R.id.txt_ekle_devamsizlik_siniri);
                final EditText txtKritikSinir = (EditText) dialog.findViewById(R.id.txt_ekle_kritik_sinir);
                final CheckBox chbSinir = (CheckBox) dialog.findViewById(R.id.cbSinir);
                final TextView txtBaslik = (TextView) dialog.findViewById(R.id.txt_baslik);

                dialog.show();
                txtBaslik.setText("Ders Güncelleme");
                btnGuncelle.setText("Güncelle");
                txtDersAdiEkle.setText(selectedDers.getAdi());
                txtDevamsizlik.setText(decimalFormat.format(selectedDers.getDevamsizlik()));
                txtDevamsizlikSiniri.setText(decimalFormat.format(selectedDers.getDevSiniri()));
                if (selectedDers.getKritikSinir() == -1)
                    chbSinir.setSelected(false);
                else txtKritikSinir.setText(decimalFormat.format(selectedDers.getKritikSinir()));
                chbSinir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                         txtKritikSinir.setEnabled(b);

                    }
                });

                SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(dialog.getContext(), R.layout.spinner_item, new ArrayList(Arrays.asList(new String[]{"Kredi", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"})));
                ((ArrayAdapter) spinnerAdapter).setDropDownViewResource(R.layout.spinner_item);
                spnKredi.setAdapter(spinnerAdapter);
                spnKredi.setSelection(selectedDers.getKredi());
                btnGuncelle.setOnClickListener(new View.OnClickListener() {
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
                            tblDers guncellenecekDers = new tblDers(dersAdi, kredi, devSiniri, devamsizlik, kritikSinir);
                            guncellenecekDers.setId(Integer.parseInt(txtId.getText().toString()));
                            dbVeriIslemMerkezi veriM = new dbVeriIslemMerkezi(dialog.getContext());


                            if (veriM.dersGuncelle(Integer.parseInt(txtId.getText().toString()),guncellenecekDers) == 1) {
                                setData(guncellenecekDers,0);
                                dialog.dismiss();
                            }
                                else
                                Toast.makeText(dialog.getContext(), "Ders güncellenemedi. Verilerinizi kontrol edin. Tekrar deneyin.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }

        private void deleteItem(int position) {
            dersList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, dersList.size());
        }

        public boolean eklemeHataKontrol(String dersAdi, double devamsizlik, double devSiniri, double kritikSinir, int kredi, boolean kritikSinirChecked) {
            if (kritikSinir >= 0 || kritikSinir == -1) {
                if (devamsizlik >= 0) {
                    if (devSiniri >= 0) {
                        if (devamsizlik >= devSiniri && kritikSinir <= devSiniri && kritikSinirChecked) {
                            Toast.makeText(context, "Devamsızlık sınırınızı aştıysanız kritik sınır belirleyemezsiniz.", Toast.LENGTH_SHORT).show();
                            return false;
                        } else if (devSiniri >= 14) {
                            Toast.makeText(context, "Devamsızlık sınırı 13 haftayı geçemez.", Toast.LENGTH_SHORT).show();
                            return false;
                        } else if (dersAdi.length() < 6) {
                            Toast.makeText(context, "Ders adı en az 6 karakter olmalıdır.", Toast.LENGTH_SHORT).show();
                            return false;
                        } else if (kredi != 0) {
                            return true;
                        } else {
                            Toast.makeText(context, "Lütfen dersin kredisini seçin.", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                }
            }
            Toast.makeText(context, "Lütfen negatif değerler girmeyin.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

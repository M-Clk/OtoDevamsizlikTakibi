package com.mclk.devamsizliktakibi;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder>  {

    List<String> stringList;
    List<Integer> idList;
    LayoutInflater inflater;
    Dialog dialog;

    public OptionsAdapter(List<String> stringList, List<Integer> idList, Context context, Dialog dialog) {
        this.stringList = stringList;
        this.idList=idList;
        inflater = LayoutInflater.from(context);
        this.dialog=dialog;
    }

    @Override
    public OptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = inflater.inflate(R.layout.item_ders_adi, parent, false); //cardView tanıtılır.
        return new OptionsAdapter.ViewHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(OptionsAdapter.ViewHolder holder, int position) {
        holder.setData(stringList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        RadioButton rdItem;
        public ViewHolder(View view) {
            super(view);
            rdItem=view.findViewById(R.id.rd_item);
            rdItem.setChecked(false);
            rdItem.setOnClickListener(this);
        }
        public void setData(String selectedString, int position) {
            rdItem.setText(selectedString);
            rdItem.setTag(idList.get(position));
        }

        @Override
        public void onClick(View view) {
            DersProgramiActivity.selectedDersName=((RadioButton)view).getText().toString();
            DersProgramiActivity.selectedId = (int)view.getTag();
            dialog.dismiss();
        }
    }
}

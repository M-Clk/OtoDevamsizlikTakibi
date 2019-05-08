package com.mclk.devamsizliktakibi;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<String> alarmNameList;
    List<Uri> alarmUriList;
    int selectedPosition;
    Context context;
    Switch selectedSwitch;


    public AlarmAdapter(Context context, List<String> alarmNameList, List<Uri> alarmUriList, int selectedPosition) {
        this.inflater = LayoutInflater.from(context);
        this.alarmNameList = alarmNameList;
        this.alarmUriList = alarmUriList;
        this.selectedPosition = 3;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = inflater.inflate(R.layout.alarm_listesi_elemani, parent, false);
        return new AlarmAdapter.ViewHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setData(alarmNameList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return alarmNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        Switch alarmSwitch;
        ImageButton btnPlay;

        @TargetApi(Build.VERSION_CODES.M)
        public ViewHolder(View view) {
            super(view);
            alarmSwitch = (Switch) view.findViewById(R.id.switch1);
            btnPlay = (ImageButton) view.findViewById(R.id.btn_alarm_play);
            alarmSwitch.setOnCheckedChangeListener(this);
        }

        public void setData(String selectedAlarmName, int position) {
            alarmSwitch.setText(selectedAlarmName);
        }
        @Override
        public void onClick(View view) {
        }
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        }
    }
}
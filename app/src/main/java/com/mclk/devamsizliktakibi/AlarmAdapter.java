package com.mclk.devamsizliktakibi;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
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
    int selectedPosition;
    Context context;
    Switch selectedSwitch;
    int selectedVoicePosition=-1;
    boolean isPlaying=false;
    ImageButton selectedImage;
    RingtoneManager ringtoneManager;


    public AlarmAdapter(Context context, List<String> alarmNameList, RingtoneManager ringtoneManager, int selectedPosition) {
        this.inflater = LayoutInflater.from(context);
        this.alarmNameList = alarmNameList;
        this.selectedPosition = selectedPosition;
        this.context = context;
        this.ringtoneManager = ringtoneManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = inflater.inflate(R.layout.alarm_listesi_elemani, parent, false); //cardView tanıtılır.
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

    @TargetApi(Build.VERSION_CODES.M)
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        Switch alarmSwitch;
        ImageButton btnPlay;
        int currentPosition;

        @TargetApi(Build.VERSION_CODES.M)
        public ViewHolder(View view) {
            super(view);
            alarmSwitch = (Switch) view.findViewById(R.id.switch1);
            btnPlay = (ImageButton) view.findViewById(R.id.btn_alarm_play);
            btnPlay.setOnClickListener(this);
            alarmSwitch.setOnCheckedChangeListener(this);
        }
        public void setData(String selectedAlarmName, int position) {
            alarmSwitch.setText(selectedAlarmName);
            currentPosition = position;
            if (position == selectedPosition) {
                alarmSwitch.setChecked(true);
                selectedSwitch = alarmSwitch;
            } else alarmSwitch.setChecked(false);

            if(position==selectedVoicePosition) {
                btnPlay.setImageResource(R.drawable.ic_stop_black_24dp);
                selectedImage=btnPlay;
            }
            else
                btnPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==btnPlay.getId())
            {
                if(selectedVoicePosition!=-1) ringtoneManager.getRingtone(selectedVoicePosition).stop();

                if(selectedVoicePosition==getAdapterPosition())
                {
                    btnPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    selectedVoicePosition=-1;
                    return;
                }

                selectedVoicePosition=getLayoutPosition();
                ringtoneManager.getRingtone(selectedVoicePosition).play();
                try {
                    selectedImage.setImageResource(R.drawable.ic_play_arrow_black_24dp);

                } catch (Exception ex) {
                }
                finally {
                    selectedImage=(ImageButton)view;
                    ((ImageButton)view).setImageResource(R.drawable.ic_stop_black_24dp);
                }
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (!b && currentPosition == selectedPosition) {
                compoundButton.setChecked(true);
                compoundButton.setTextColor(compoundButton.getResources().getColor(R.color.colorPrimary));
                return;
            }
            if (!b)
            {
                compoundButton.setChecked(false);
                compoundButton.setTextColor(compoundButton.getResources().getColor(R.color.colorPrimaryDark));
            }
            else {
                selectedPosition = currentPosition;
                try {
                    selectedSwitch.setChecked(false);
                } catch (Exception ex) {
                }
                finally {
                    selectedSwitch=(Switch) compoundButton;
                    selectedSwitch.setTextColor(compoundButton.getResources().getColor(R.color.colorPrimary));
                    AlarmOptionsActivity.selectedOptionAlarm = alarmNameList.get(selectedPosition)+"|"+selectedPosition;
                }
            }

        }
    }
}
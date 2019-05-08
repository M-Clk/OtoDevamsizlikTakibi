package com.mclk.devamsizliktakibi;

public class tblAlarm {
    int id;
    String alarmAdi;
    public tblAlarm(int id, String alarmAdi) {
        this.id = id;
        this.alarmAdi = alarmAdi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlarmAdi() {
        return alarmAdi;
    }

    public void setAlarmAdi(String alarmAdi) {
        this.alarmAdi = alarmAdi;
    }

}

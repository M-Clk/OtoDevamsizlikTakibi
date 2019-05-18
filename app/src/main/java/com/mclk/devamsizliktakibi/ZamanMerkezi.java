package com.mclk.devamsizliktakibi;

import java.util.Date;

public class ZamanMerkezi {
    public int ClockToInt(int i, int i2) {
        return (i * 60) + i2;
    }

    public static  int dateToUnix(Date date) {
        return (int) (date.getTime() / 1000);
    }

    public Date unixToDate(int i) {
        return new Date(((long) i) * 1000);
    }

    public int IntToHours(int i) {
        return i /60;
    }

    public int IntToMinutes(int i) {
        return i % 60;
    }

    public Date IntToDate(int i) {
        Date date = new Date();
        date.setMinutes(IntToMinutes(i));
        date.setHours(IntToHours(i));
        return date;
    }
    public int DateToInt(Date date) {
        return (date.getHours() * 60) + date.getMinutes();
    }
}
package com.mclk.devamsizliktakibi;

public class tblDers {
    String adi;
    double devSiniri;
    double devamsizlik;
    int id;
    int kredi;
    double kritikSinir;

    public tblDers(String adi, int kredi, double devSiniri, double devamsizlik, double kritikSinir) {
        this.kritikSinir = kritikSinir;
        this.kredi = kredi;
        this.devSiniri = devSiniri;
        this.devamsizlik = devamsizlik;
        this.adi = adi;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKredi() {
        return this.kredi;
    }

    public double getDevSiniri() {
        return this.devSiniri;
    }

    public double getDevamsizlik() {
        return this.devamsizlik;
    }

    public String getAdi() {
        return this.adi;
    }

    public double getKritikSinir() {
        return this.kritikSinir;
    }
}

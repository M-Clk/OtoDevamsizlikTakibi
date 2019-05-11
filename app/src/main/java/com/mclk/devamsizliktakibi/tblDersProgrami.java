package com.mclk.devamsizliktakibi;

public class tblDersProgrami {
    private int Id;
    private int basSaati;
    private int bitSaati;
    private String dersAdi;
    private int dersId;
    private int gunId;

    public tblDersProgrami(int id, int dersId, String dersAdi, int gunId, int basSaati, int bitSaati) {
        this.Id = id;
        this.dersId = dersId;
        this.gunId = gunId;
        this.basSaati = basSaati;
        this.bitSaati = bitSaati;
        this.dersAdi = dersAdi;
    }

    public int getId() {
        return this.Id;
    }

    public int getDersId() {
        return this.dersId;
    }

    public int getGunId() {
        return this.gunId;
    }

    public int getBasSaati() {
        return this.basSaati;
    }

    public int getBitSaati() {
        return this.bitSaati;
    }

    public String getDersAdi() {
        return this.dersAdi;
    }
}

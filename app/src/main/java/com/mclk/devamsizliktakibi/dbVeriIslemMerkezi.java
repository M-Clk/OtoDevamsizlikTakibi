package com.mclk.devamsizliktakibi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class dbVeriIslemMerkezi {
    SQLiteDatabase database;
    dbSqlKatmani dbsqlKatmani;
    SharedPreferences settingValues;

    public dbVeriIslemMerkezi(Context context) {
        this.dbsqlKatmani = new dbSqlKatmani(context, "dbDevamsizlikTakibi", null, 1);
    }

    private void Ac() {
        this.database = this.dbsqlKatmani.getWritableDatabase();
    }


    public int dersEkle(com.mclk.devamsizliktakibi.tblDers eklenecekDers) {

        Ac();
        ContentValues values = new ContentValues();
        values.put("adi",  eklenecekDers.getAdi());
        values.put("kredi",  eklenecekDers.getKredi());
        values.put("devSiniri",  eklenecekDers.getDevSiniri());
        values.put("devamsizlik",  eklenecekDers.getDevamsizlik());
        values.put("kritikSinir",  eklenecekDers.getKritikSinir());

        int sonuc = (int) database.insert("Dersler", null, values);
        database.close();
        return sonuc;
    }

    public List<tblDers> dersListele() {
        Ac();
        String[] strArr = new String[]{"id", "adi", "kredi", "devSiniri", "devamsizlik", "kritikSinir"};
        List<tblDers> arrayList = new ArrayList<tblDers>();
        Cursor query = this.database.query("Dersler", strArr, null, null, null, null, "devamsizlik DESC");
        query.moveToFirst();
        while (!query.isAfterLast()) {
            int id = query.getInt(0);
            tblDers tblders = new tblDers(query.getString(1), query.getInt(2), query.getDouble(3), query.getDouble(4), query.getDouble(5));
            tblders.setId(id);
            arrayList.add(tblders);
            query.moveToNext();
        }
        query.close();
        database.close();
        return arrayList;
    }

    public int dersGuncelle(int dersId, tblDers oldDers) {

        Ac();
        ContentValues values = new ContentValues();
        values.put("adi", oldDers.getAdi());
        values.put("kredi", oldDers.getKredi());
        values.put("devSiniri", oldDers.getDevSiniri());
        values.put("devamsizlik", oldDers.getDevamsizlik());
        values.put("kritikSinir", oldDers.getKritikSinir());

        int guncellenenSayisi = database.update("Dersler",values,"id="+dersId,null);
        database.close();
        return guncellenenSayisi;
    }

    public int dersSil(String dersId)
    {
        Ac();
       int  silinenSayisi = database.delete("Dersler","id="+dersId,null);
        database.close();
        return silinenSayisi;
    }

    public double DevamsizlikArttir(int dersId) {

        Ac();


        database.close();
        return 0;
    }
    public tblDers TekDersSorgula(int dersId) {

        Ac();

        database.close();
        return new tblDers("",0,0,0,0);
    }
    public int dersProgramiEkle(tblDersProgrami eklenecekDersPrg) {
        Ac();
        ContentValues values = new ContentValues();
        values.put("gunId", eklenecekDersPrg.getGunId());
        values.put("dersId", eklenecekDersPrg.getDersId());
        values.put("basSaati", eklenecekDersPrg.getBasSaati());
        values.put("bitSaati", eklenecekDersPrg.getBitSaati());

        int eklenenSayisi = (int) database.insert("DersProgrami", null, values);

        database.close();
        return eklenenSayisi;
    }
    public List<tblDersProgrami> dersProgramiListele(int gunId) {
        Ac();
        String queryString = "SELECT DersProgrami.id, Dersler.id, Dersler.adi, gunId, basSaati, bitSaati FROM Dersler,DersProgrami WHERE DersProgrami.dersId=Dersler.id AND gunId="+gunId+" Order By basSaati ASC";
        Log.d("query", queryString);
        List<tblDersProgrami> arrayList = new ArrayList<tblDersProgrami>();
        Cursor rawQuery = this.database.rawQuery(queryString, null);
        rawQuery.moveToFirst();
        while (!rawQuery.isAfterLast()) {
            arrayList.add(new tblDersProgrami(rawQuery.getInt(0), rawQuery.getInt(1), rawQuery.getString(2), gunId, rawQuery.getInt(4), rawQuery.getInt(5)));
            rawQuery.moveToNext();
        }
        rawQuery.close();
        database.close();
        return arrayList;
    }
    public int DersProgramiSil(String prgId) {
        Ac();
        int  silinenSayisi = database.delete("DersProgrami","id="+prgId,null);
        database.close();
        return silinenSayisi;
    }

    public double dersSayisiAl(int dersId) {
        Ac();
        String queryStr="SELECT dersId FROM DersProgrami WHERE dersId="+dersId;
        Log.d("query", queryStr);
        Cursor cursor = this.database.rawQuery(queryStr, null);
        int count = cursor.getCount();
        database.close();
        return (double) count;
    }

    public void bildirimVeAlarmYukle(Context context) {
        Ac();
        Context context2 = context;
        this.settingValues = context2.getSharedPreferences("com.mclk.devamsizliktakibi", 0);
        Intent intent = new Intent(context2, UyariDinleyici.class);
        Ac();
        ZamanMerkezi zamanMerkezi = new ZamanMerkezi();
        Intent intent2 = new Intent(context2, AlarmDinleyici.class);
        int i = 0;
        while (i < 5) {
            ZamanMerkezi zamanMerkezi2;
            List dersProgramiListele = dersProgramiListele(i);
            int j = 0;
            while (j < dersProgramiListele.size()) {
                int i3;
                Date unixToDate = zamanMerkezi.unixToDate(((tblDersProgrami) dersProgramiListele.get(j)).getBasSaati());
                int minutes = unixToDate.getMinutes();
                int hours = unixToDate.getHours();
                Date unixToDate2 = zamanMerkezi.unixToDate(((tblDersProgrami) dersProgramiListele.get(j)).getBitSaati());
                int minutes2 = unixToDate2.getMinutes();
                int hours2 = unixToDate2.getHours();
                int i4 = settingValues.getInt("sorguZamani", 10) + minutes2;
                if (i4 >= 60) {
                    i4 -= 60;
                    i3 = hours2 + 1;
                    if (i3 > 23) {
                        i3 = 0;
                    }
                } else {
                    i3 = hours2;
                }
                intent.putExtra("dersAdi", ((tblDersProgrami) dersProgramiListele.get(j)).getDersAdi());
                zamanMerkezi2 = zamanMerkezi;
                Intent intent3 = intent2;
              //  String dersSaati=String.format("%02d", hours)+":"+String.format("%02d", minutes)+" - "+String.format("%02d",hours2)+":"+String.format("%02d", minutes2);
               String dersSaati = "Ornek";
                intent.putExtra("dersSaati", dersSaati);
                intent.putExtra("dersId", ((tblDersProgrami) dersProgramiListele.get(j)).getDersId());
                intent.putExtra("NOTIFICATION_ID", ((tblDersProgrami) dersProgramiListele.get(j)).getId());
                Calendar instance = Calendar.getInstance();
                instance.set(Calendar.MILLISECOND, 0);
                instance.set(Calendar.SECOND, 0);
                instance.set(Calendar.MINUTE, i4);
                instance.set(Calendar.HOUR, i3);
                switch (i) {
                    case 0:
                        instance.set(Calendar.DAY_OF_WEEK, Calendar.MONTH);
                        break;
                    case 1:
                        instance.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                        break;
                    case 2:
                        instance.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                        break;
                    case 3:
                        instance.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                        break;
                    case 4:
                        instance.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                        break;
                    default:
                        break;
                }
                if (Calendar.getInstance().after(instance)) {
                    instance.add(Calendar.WEEK_OF_MONTH, 1);
                }


                AlarmManager bildirimAlarmi=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
              //  bildirimAlarmi.setInexactRepeating(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), 604800000, PendingIntent.getBroadcast(context2, ((tblDersProgrami) dersProgramiListele.get(i2)).getId(), intent, 0));
                Intent kritikAlarmIntent = intent3;
                kritikAlarmIntent.putExtra("dersId", ((tblDersProgrami) dersProgramiListele.get(j)).getDersId());
                kritikAlarmIntent.putExtra("alarmId", -((tblDersProgrami) dersProgramiListele.get(j)).getId());
                kritikAlarmIntent.putExtra("dersAdi", ((tblDersProgrami) dersProgramiListele.get(j)).getDersAdi());
                minutes -= this.settingValues.getInt("alarmZamani", 60);
                while (minutes < 0) {
                    minutes += 60;
                    hours--;
                    if (hours < 0) {
                        hours = 23;
                    }
                }
                instance = Calendar.getInstance();
                instance.set(Calendar.MINUTE, minutes);
                instance.set(Calendar.HOUR, hours);
                instance.set(Calendar.MILLISECOND, 0);
                instance.set(Calendar.SECOND, 0);
                instance.set(Calendar.DAY_OF_WEEK, i + 2);
        //  bildirimAlarmi.setInexactRepeating(0, instance.getTimeInMillis(), 604800000, PendingIntent.getBroadcast(context2, -((tblDersProgrami) dersProgramiListele.get(i2)).getId(), intent4, 0));
                j++;
          //      intent2 = intent4;
                zamanMerkezi = zamanMerkezi2;
            }
            zamanMerkezi2 = zamanMerkezi;
            i++;
            intent2 = intent2;

        }
        database.close();}

}

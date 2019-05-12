package com.mclk.devamsizliktakibi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class dbVeriIslemMerkezi {
    SQLiteDatabase database;
    dbSqlKatmani dbsqlKatmani;
    String defaultBaslangic = "1 Saat Önce|" + 60,
            defaultSorguZamani = "Dersin Bittiği An|" + 0;


    public dbVeriIslemMerkezi(Context context) {
        this.dbsqlKatmani = new dbSqlKatmani(context, "dbDevamsizlikTakibi", null, 1);
    }

    private void Ac() {
        this.database = this.dbsqlKatmani.getWritableDatabase();
    }


    public int dersEkle(com.mclk.devamsizliktakibi.tblDers eklenecekDers) {

        Ac();
        ContentValues values = new ContentValues();
        values.put("adi", eklenecekDers.getAdi());
        values.put("kredi", eklenecekDers.getKredi());
        values.put("devSiniri", eklenecekDers.getDevSiniri());
        values.put("devamsizlik", eklenecekDers.getDevamsizlik());
        values.put("kritikSinir", eklenecekDers.getKritikSinir());

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

        int guncellenenSayisi = database.update("Dersler", values, "id=" + dersId, null);
        database.close();
        return guncellenenSayisi;
    }

    public int dersSil(String dersId) {
        Ac();
        int silinenSayisi;
        try {
            silinenSayisi = database.delete("Dersler", "id=" + dersId, null);
        } catch (Exception ex) {
            silinenSayisi = 0;
        } finally {
            database.close();
        }
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
        return new tblDers("", 0, 0, 0, 0);
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
        String queryString = "SELECT DersProgrami.id, Dersler.id, Dersler.adi, gunId, basSaati, bitSaati FROM Dersler,DersProgrami WHERE DersProgrami.dersId=Dersler.id AND gunId=" + gunId + " Order By basSaati ASC";
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
        int silinenSayisi;
        try {
            silinenSayisi = database.delete("DersProgrami", "id=" + prgId, null);
        } catch (Exception ex) {
            silinenSayisi = 0;
        } finally {
            database.close();
        }
        return silinenSayisi;
    }

    public double dersSayisiAl(int dersId) {
        Ac();
        String queryStr = "SELECT dersId FROM DersProgrami WHERE dersId=" + dersId;
        Log.d("query", queryStr);
        Cursor cursor = this.database.rawQuery(queryStr, null);
        int count = cursor.getCount();
        database.close();
        return (double) count;
    }

    public void bildirimVeAlarmYukle(Context context) {
        Ac();
        Intent uyariIntent = new Intent(context, UyariDinleyici.class);
        Intent alarmIntent = new Intent(context, AlarmDinleyici.class);
        Calendar startLessonClndr = Calendar.getInstance();
        Calendar finishLessonClndr = Calendar.getInstance();
        for (int i = 0; i < 5; i++) {
            List dersProgramiListele = dersProgramiListele(i);
            for (int j = 0; j < dersProgramiListele.size(); j++) {

                startLessonClndr.setTimeInMillis(0);
                finishLessonClndr.setTimeInMillis(0);


                startLessonClndr.add(Calendar.MINUTE, ((tblDersProgrami) dersProgramiListele.get(j)).getBasSaati());
                finishLessonClndr.add(Calendar.MINUTE, ((tblDersProgrami) dersProgramiListele.get(j)).getBitSaati());
                //Aldım.
                String sorguZamaniName = context.getResources().getResourceEntryName(R.id.txt_sorgu_zamani);
                String strSorguZamani = MainActivity.settingValues.getString(sorguZamaniName, defaultSorguZamani);
                strSorguZamani = strSorguZamani.substring(strSorguZamani.indexOf("|" + 1));
                int sorguGecikmesi = Integer.parseInt(strSorguZamani);
                //
                String alarmBaslangiName = context.getResources().getResourceEntryName(R.id.text_baslangic_zamani);
                String strBaslangicName = MainActivity.settingValues.getString(alarmBaslangiName, defaultBaslangic);
                strBaslangicName = strBaslangicName.substring(strBaslangicName.indexOf("|" + 1));
                int alarmBaslangici = Integer.parseInt(strBaslangicName);
                //

                alarmIntent.putExtra("dersAdi", ((tblDersProgrami) dersProgramiListele.get(j)).getDersAdi());

                String dersSaati = String.format("%02d", startLessonClndr.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", startLessonClndr.get(Calendar.MINUTE)) + " - " + String.format("%02d", finishLessonClndr.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", finishLessonClndr.get(Calendar.MINUTE));
                finishLessonClndr.add(Calendar.MINUTE, sorguGecikmesi); //Ders saatini kaydettikten sonra sorgu süresini ekle
                startLessonClndr.add(Calendar.MINUTE, -alarmBaslangici); //Alarm saatini ekle
                uyariIntent.putExtra("dersSaati", dersSaati);
                uyariIntent.putExtra("dersId", ((tblDersProgrami) dersProgramiListele.get(j)).getDersId());
                uyariIntent.putExtra("NOTIFICATION_ID", ((tblDersProgrami) dersProgramiListele.get(j)).getId());
                Calendar instance = Calendar.getInstance();
                instance.set(Calendar.MILLISECOND, 0);
                instance.set(Calendar.SECOND, 0);
                instance.set(Calendar.MINUTE, finishLessonClndr.get(Calendar.MINUTE));
                instance.set(Calendar.HOUR_OF_DAY, finishLessonClndr.get(Calendar.HOUR_OF_DAY));
                instance.set(Calendar.DAY_OF_WEEK, i + 2);

                if (Calendar.getInstance().after(instance)) {
                    instance.add(Calendar.WEEK_OF_MONTH, 1);
                }

                AlarmManager bildirimAlarmi = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                bildirimAlarmi.setInexactRepeating(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), 604800000, PendingIntent.getBroadcast(context, ((tblDersProgrami) dersProgramiListele.get(j)).getId(), uyariIntent, 0));
                alarmIntent.putExtra("dersId", ((tblDersProgrami) dersProgramiListele.get(j)).getDersId());
                alarmIntent.putExtra("alarmId", -((tblDersProgrami) dersProgramiListele.get(j)).getId());
                alarmIntent.putExtra("dersAdi", ((tblDersProgrami) dersProgramiListele.get(j)).getDersAdi());

                instance = Calendar.getInstance();
                instance.set(Calendar.MILLISECOND, 0);
                instance.set(Calendar.SECOND, 0);
                instance.set(Calendar.MINUTE, startLessonClndr.get(Calendar.MINUTE));
                instance.set(Calendar.HOUR_OF_DAY, startLessonClndr.get(Calendar.HOUR_OF_DAY));
                instance.set(Calendar.DAY_OF_WEEK, i + 2);
                bildirimAlarmi.setInexactRepeating(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), 604800000, PendingIntent.getBroadcast(context, -((tblDersProgrami) dersProgramiListele.get(j)).getId(), alarmIntent, 0));
            }
        }
        database.close();
    }

}

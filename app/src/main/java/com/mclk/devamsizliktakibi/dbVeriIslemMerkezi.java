package com.mclk.devamsizliktakibi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public String DevamsizlikArttir(int dersId) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double dersSayisi=dersSayisiAl(dersId);
        double artanDev=1/dersSayisi;
        tblDers devArtmisDers = TekDersSorgula(dersId);
        if(devArtmisDers==null)
            return null;
        Ac();
        String strSonuc = decimalFormat.format(devArtmisDers.getDevamsizlik())+" hafta olan "+devArtmisDers.getAdi()+" dersinin devamsızlığı ";
                database.execSQL("UPDATE Dersler SET devamsizlik=devamsizlik+"+artanDev+" Where id="+dersId);
        database.close();
        devArtmisDers = TekDersSorgula(dersId);
        strSonuc+=decimalFormat.format(devArtmisDers.getDevamsizlik())+" hafta oldu.";

        return strSonuc;
    }

    public tblDers TekDersSorgula(int dersId) {

        Ac();
        String strQuery="SELECT  adi, kredi, devSiniri, devamsizlik, kritikSinir,id FROM Dersler WHERE id="+dersId;

        Cursor queryCursor = database.rawQuery(strQuery,null);
        if(queryCursor.getCount()==1)
        {
        queryCursor.moveToFirst();
            tblDers tblders = new tblDers(queryCursor.getString(0), queryCursor.getInt(1), queryCursor.getDouble(2), queryCursor.getDouble(3), queryCursor.getDouble(4));
            tblders.setId(queryCursor.getInt(5));
        queryCursor.close();
        database.close();
        return tblders;}
        else return null;
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
        Cursor cursor = this.database.rawQuery(queryStr, null);
        int count = cursor.getCount();
        database.close();
        return (double) count;
    }

    public void bildirimVeAlarmYukle(Context context) {
        try {
            Ac();
            Intent uyariIntent = new Intent(context, UyariDinleyici.class);
            Intent alarmIntent = new Intent(context, AlarmDinleyici.class);
            Calendar startLessonClndr;
            Calendar finishLessonClndr;
            for (int i = 0; i < 5; i++) {
                List dersProgramiListele = dersProgramiListele(i);
                for (int j = 0; j < dersProgramiListele.size(); j++) {

                    tblDersProgrami selectedDersPrg=(tblDersProgrami) dersProgramiListele.get(j);
                    startLessonClndr = getCleanCalendar(i+2);
                    finishLessonClndr = getCleanCalendar(i+2);

                    startLessonClndr.add(Calendar.MINUTE, selectedDersPrg.getBasSaati());
                    finishLessonClndr.add(Calendar.MINUTE, selectedDersPrg.getBitSaati());
                    //Aldım.
                    String sorguZamaniName = context.getResources().getResourceEntryName(R.id.txt_sorgu_zamani);
                    String strSorguZamani = MainActivity.settingValues.getString(sorguZamaniName, defaultSorguZamani);
                    strSorguZamani = strSorguZamani.substring(strSorguZamani.indexOf('|' )+ 1);
                    int sorguGecikmesi = Integer.parseInt(strSorguZamani);
                    //
                    String alarmBaslangiName = context.getResources().getResourceEntryName(R.id.text_baslangic_zamani);
                    String strBaslangicName = MainActivity.settingValues.getString(alarmBaslangiName, defaultBaslangic);
                    strBaslangicName = strBaslangicName.substring(strBaslangicName.indexOf('|' )+ 1);
                    int alarmBaslangici = Integer.parseInt(strBaslangicName);

                    //

                    int notifyId=selectedDersPrg.getId();

                    String dersSaati = String.format("%02d", startLessonClndr.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", startLessonClndr.get(Calendar.MINUTE)) + " - " + String.format("%02d", finishLessonClndr.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", finishLessonClndr.get(Calendar.MINUTE));
                    finishLessonClndr.add(Calendar.MINUTE, sorguGecikmesi); //Ders saatini kaydettikten sonra sorgu süresini ekle
                    startLessonClndr.add(Calendar.MINUTE, -alarmBaslangici); //Alarm saatini ekle
                    uyariIntent.putExtra("dersSaati", dersSaati);
                    uyariIntent.putExtra("dersAdi", selectedDersPrg.getDersAdi());
                    uyariIntent.putExtra("dersId", selectedDersPrg.getDersId());
                    uyariIntent.putExtra("NOTIFICATION_ID", notifyId);

                    alarmIntent.putExtra("dersId",selectedDersPrg.getDersId());
                    alarmIntent.putExtra("alarmId", -notifyId);
                    alarmIntent.putExtra("dersAdi", selectedDersPrg.getDersAdi());

                    if (Calendar.getInstance().after(finishLessonClndr))
                        finishLessonClndr.add(Calendar.WEEK_OF_MONTH, 1);
                    if (Calendar.getInstance().after(startLessonClndr))
                        startLessonClndr.add(Calendar.WEEK_OF_MONTH, 1);

                    AlarmManager bildirimAlarmi = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    bildirimAlarmi.setInexactRepeating(AlarmManager.RTC_WAKEUP, finishLessonClndr.getTimeInMillis(), 604800000, PendingIntent.getBroadcast(context, notifyId, uyariIntent, 0));
                    bildirimAlarmi.setInexactRepeating(AlarmManager.RTC_WAKEUP, startLessonClndr.getTimeInMillis(), 604800000, PendingIntent.getBroadcast(context, -notifyId, alarmIntent, 0));
                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_LONG).show();
        }
       finally {
            database.close();
        }
    }
    Calendar getCleanCalendar(int dayOfWeek)
    {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.MILLISECOND, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.HOUR_OF_DAY,0);
        instance.set(Calendar.MINUTE,0);
        instance.set(Calendar.DAY_OF_WEEK,dayOfWeek);
        return  instance;
    }

}

package com.mclk.devamsizliktakibi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class dbSqlKatmani extends SQLiteOpenHelper {
    public dbSqlKatmani(Context context, String str, CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE Dersler(id INTEGER PRIMARY KEY AUTOINCREMENT, adi TEXT NOT NULL,kredi INTEGER, devSiniri REAL,devamsizlik REAL,kritikSinir REAL)");
        sQLiteDatabase.execSQL("CREATE TABLE DersProgrami(id INTEGER PRIMARY KEY AUTOINCREMENT, gunId INTEGER, dersId INTEGER, basSaati INTEGER, bitSaati INTEGER,FOREIGN KEY (dersId) REFERENCES Dersler(id))");
        sQLiteDatabase.execSQL("CREATE TABLE TatilGunleri (dayOfYear INTEGER PRIMARY KEY, holidayTag TEXT)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("drop table if exists Dersler");
        sQLiteDatabase.execSQL("drop table if exists DersProgrami");
        sQLiteDatabase.execSQL("drop table if exists TatilGunleri");
    }

    public void onOpen(SQLiteDatabase sQLiteDatabase) {
        super.onOpen(sQLiteDatabase);
        if (!sQLiteDatabase.isReadOnly()) {
            sQLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}

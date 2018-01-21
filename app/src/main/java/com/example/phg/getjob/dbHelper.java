package com.example.phg.getjob;

/**
 * Created by PHG on 2018-01-15.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {
    public dbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//테이블 생성
        db.execSQL("CREATE TABLE LIKE_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String _query) {//db추가
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void delete(String _query) {//db삭제
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public String PrintData() {//text에 추가할 내용
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("select * from LIKE_LIST", null);
        while (cursor.moveToNext()) {
            str += "  "
                    + cursor.getString(1)//스트링값 받아서
                    + " 날짜 "
                    + cursor.getString(2)//int값 받아서
                    + "\n";
        }
        return str;
    }
}

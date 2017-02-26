package com.me.android.recording.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.me.android.recording.database.RecordDbSchema.RecordTable;

/**
 * Created by ASUS on 2016/12/6.
 */
public class RecordBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "recordBase.db";

    public RecordBaseHelper (Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(" create table  " + RecordTable.NAME + "(" +
                "_id integer primary key autoincrement," +
                          RecordTable.CoIs.UUID + "," +
                          RecordTable.CoIs.TITLE + "," +
                          RecordTable.CoIs.PHONE + "," +
                          RecordTable.CoIs.DATE + "," +
                          RecordTable.CoIs.SOLVED + ")"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}

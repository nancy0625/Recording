package com.me.android.recording;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.me.android.recording.database.RecordBaseHelper;
import com.me.android.recording.database.RecordCursorWrapper;
import com.me.android.recording.database.RecordDbSchema.RecordTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ASUS on 2016/11/26.
 */
public class RecordLab {
    private static RecordLab sRecordLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    public static RecordLab get(Context context){
        if (sRecordLab == null){
            sRecordLab = new RecordLab(context);
        }
        return sRecordLab;
    }
    private RecordLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new RecordBaseHelper(mContext).getWritableDatabase();


    }

    public void addRecord(Record record){
        ContentValues contentValues = getContentValues(record);
        mDatabase.insert(RecordTable.NAME,null,contentValues);

    }
    public List<Record> getRecords(){
      List <Record> recordList = new ArrayList<>();
        RecordCursorWrapper cursorWrapper = queryRecords(null,null);
        try{
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                recordList.add(cursorWrapper.getRecord());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }
        return recordList;
    }
    public Record getRecord(UUID id){
        RecordCursorWrapper cursorWrapper = queryRecords(
                RecordTable.CoIs.UUID + "=?",
                new String[]{id.toString()}
        );
        try{
            if (cursorWrapper.getCount() == 0){
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getRecord();
        }finally {
            cursorWrapper.close();
        }

    }

    public File getPhotoFile(Record record){
        File externalFilesDir = mContext
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null){
            return null;
        }
        return new File(externalFilesDir,record.getPhotoFilename());
    }

    public void updateRecord(Record record){
        String uuidString = record.getId().toString();
        ContentValues contentValues = getContentValues(record);
        mDatabase.update(RecordTable.NAME,contentValues,
                RecordTable.CoIs.UUID + "=?",
                new String[]{uuidString});
    }

    private static ContentValues getContentValues(Record record){
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecordTable.CoIs.UUID, record.getId().toString());
        contentValues.put(RecordTable.CoIs.TITLE, record.getTitle());
        contentValues.put(RecordTable.CoIs.PHONE, record.getPhone());
        contentValues.put(RecordTable.CoIs.DATE, record.getDate().getTime());
        contentValues.put(RecordTable.CoIs.SOLVED, record.isSolved());
        return contentValues;
    }

    private RecordCursorWrapper queryRecords(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
            RecordTable.NAME,
            null,//Columns - null select all columns
            whereClause,
            whereArgs,
            null,//groupBy
            null,//having
            null//orderBy
        );
        return new RecordCursorWrapper(cursor);
    }
}

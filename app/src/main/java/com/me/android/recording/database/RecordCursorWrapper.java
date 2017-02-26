package com.me.android.recording.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.me.android.recording.Record;
import com.me.android.recording.database.RecordDbSchema.RecordTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ASUS on 2016/12/6.
 */
public class RecordCursorWrapper extends CursorWrapper {
    public RecordCursorWrapper(Cursor cursor){
        super(cursor);
    }
    public Record getRecord(){
        String uuidString = getString(getColumnIndex(RecordTable.CoIs. UUID));
        String title = getString(getColumnIndex(RecordTable.CoIs.TITLE));
        String phone = getString(getColumnIndex(RecordTable.CoIs.PHONE));
        long date = getLong(getColumnIndex(RecordTable.CoIs.DATE));
        int isSolved = getInt(getColumnIndex(RecordTable.CoIs.SOLVED));


        Record record = new Record(UUID.fromString(uuidString));
        record.setTitle(title);
        record.setPhone(phone);
        record.setDate(new Date(date));
        record.setSolved(isSolved != 0);

        return record;
    }
}

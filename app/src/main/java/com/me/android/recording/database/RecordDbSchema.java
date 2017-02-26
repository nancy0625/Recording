package com.me.android.recording.database;

/**
 * Created by ASUS on 2016/12/6.
 */
public class RecordDbSchema {
    public static final class RecordTable{
        public static final String NAME = "records";
        public static final class CoIs{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String PHONE = "phone";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }
}

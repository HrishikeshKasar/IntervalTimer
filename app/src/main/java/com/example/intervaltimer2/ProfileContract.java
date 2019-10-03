package com.example.intervaltimer2;

import android.provider.BaseColumns;

public class ProfileContract {
    private ProfileContract() {}

    public static final class ProfileEntry implements BaseColumns {
        public static final String TABLE_NAME = "ProfileList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_WARMUP_TIME = "warmuptime";
        public static final String COLUMN_LOWINT_TIME = "lowInttime";
        public static final String COLUMN_HIGHINT_TIME = "highInttime";
        public static final String COLUMN_SETS = "sets";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}

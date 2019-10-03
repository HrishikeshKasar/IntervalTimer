package com.example.intervaltimer2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.intervaltimer2.ProfileContract.*;
import android.support.annotation.Nullable;

public class ProfileDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "profilelist.db";
    public static final int DATABASE_VERSION = 1;

    public ProfileDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_PROFILELIST_TABLE = "CREATE TABLE " +
                ProfileEntry.TABLE_NAME + " (" +
                ProfileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProfileEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ProfileEntry.COLUMN_WARMUP_TIME + " LONG NOT NULL, " +
                ProfileEntry.COLUMN_LOWINT_TIME + " LONG NOT NULL, " +
                ProfileEntry.COLUMN_HIGHINT_TIME + " LONG NOT NULL, " +
                ProfileEntry.COLUMN_SETS + " LONG NOT NULL, " +
                ProfileEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_PROFILELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProfileEntry.TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name,long warmupTime, long lowIntTime,long HighIntTime,long sets) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ProfileEntry.COLUMN_NAME,name);
        cv.put(ProfileEntry.COLUMN_WARMUP_TIME,warmupTime);
        cv.put(ProfileEntry.COLUMN_LOWINT_TIME,lowIntTime);
        cv.put(ProfileEntry.COLUMN_HIGHINT_TIME,HighIntTime);
        cv.put(ProfileEntry.COLUMN_SETS,sets);

        long result = db.insert(ProfileEntry.TABLE_NAME,null,cv);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+ ProfileEntry.TABLE_NAME,null);
        return res;
        //the function which calls it can check res.getcount() == 0 to see the returned count
    }

    public Cursor getSpecificData(String Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+ ProfileEntry.TABLE_NAME + " WHERE _ID = ?",new String[] {Id});
        return res;
    }

    public boolean updateData(String Id, String name,long warmupTime, long lowIntTime,long HighIntTime,long sets) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ProfileEntry._ID,Id);
        cv.put(ProfileEntry.COLUMN_NAME,name);
        cv.put(ProfileEntry.COLUMN_WARMUP_TIME,warmupTime);
        cv.put(ProfileEntry.COLUMN_LOWINT_TIME,lowIntTime);
        cv.put(ProfileEntry.COLUMN_HIGHINT_TIME,HighIntTime);
        cv.put(ProfileEntry.COLUMN_SETS,sets);

        db.update(ProfileEntry.TABLE_NAME, cv, "_ID = ?", new String[] {Id});

        return true;
    }

    public int deleteData(String Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ProfileEntry.TABLE_NAME,"_ID = ?", new String[]{Id});
    }
}

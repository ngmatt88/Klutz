package com.duckwarlocks.klutz.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ngmat_000 on 7/3/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_SAVED_LOCATIONS = "SavedLocations";
    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_LOC_TITLE = "title";
    public static final String COLUMN_LOC_LATITUDE = "latitude";
    public static final String COLUMN_LOC_LONGITUDE = "longitude";
    public static final String COLUMN_LOC_CITY = "city";

    private static final String DATABASE_NAME = "location.db";
    private static final int DATABASE_VERSION = 1;

    //make the database
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SAVED_LOCATIONS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_LOC_TITLE + " text not null, "
            + COLUMN_LOC_LATITUDE + " text not null, "
            + COLUMN_LOC_LONGITUDE + " text not null, "
            + COLUMN_LOC_CITY + " text not null "
            + ");";

    public SQLiteHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion +
                        " to " + newVersion +
                        ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_SAVED_LOCATIONS);
        onCreate(db);
    }
}

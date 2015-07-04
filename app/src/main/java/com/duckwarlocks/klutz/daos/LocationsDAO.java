package com.duckwarlocks.klutz.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.duckwarlocks.klutz.utilities.SQLiteHelper;
import com.duckwarlocks.klutz.vo.LocationVO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ngmat_000 on 7/3/2015.
 */
public class LocationsDAO {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {
            SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_LOC_TITLE,
            SQLiteHelper.COLUMN_LOC_LATITUDE,
            SQLiteHelper.COLUMN_LOC_LONGITUDE,
            SQLiteHelper.COLUMN_LOC_CITY
    };

    public LocationsDAO(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public LocationVO createLocationVO(
            String title,String latitude,String longitude, String city){
        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.COLUMN_LOC_TITLE,title);
        values.put(SQLiteHelper.COLUMN_LOC_LATITUDE,latitude);
        values.put(SQLiteHelper.COLUMN_LOC_LONGITUDE,longitude);
        values.put(SQLiteHelper.COLUMN_LOC_CITY, city);

        long insertId = database.insert(
                SQLiteHelper.TABLE_SAVED_LOCATIONS, null, values);

        Cursor cursor = database.query(
                SQLiteHelper.TABLE_SAVED_LOCATIONS,
                allColumns,
                SQLiteHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);

        cursor.moveToFirst();

        LocationVO newLocation = cursorToVideo(cursor);
        cursor.close();

        return newLocation;
    }

    private LocationVO cursorToVideo(Cursor cursor) {
        LocationVO location = new LocationVO();
        location.setDbId(cursor.getLong(0));
        location.setmName(cursor.getString(1));
        location.setmLatitude(Double.parseDouble(cursor.getString(2)));
        location.setmLongitude(Double.parseDouble(cursor.getString(3)));
        location.setmCity(cursor.getString(4));

        return location;
    }


    public void deleteLocationById(long dbId) {
        long id = dbId;
        System.out.println("Location deleted with id: " + id);
        database.delete(
                SQLiteHelper.TABLE_SAVED_LOCATIONS,
                SQLiteHelper.COLUMN_ID
                        + " = " + id, null);
    }

    public void deleteLocation(LocationVO locationVO) {
        long id = locationVO.getDbId();
        System.out.println("Location deleted with id: " + id);
        try{
            open();
            database.delete(
                    SQLiteHelper.TABLE_SAVED_LOCATIONS,
                    SQLiteHelper.COLUMN_ID
                            + " = " + id, null);
            close();
        }catch (SQLException e){
            Log.e(getClass().getName(),e.toString());
            e.printStackTrace();
        }

    }

    public List<LocationVO> getAllVideos() {
        List<LocationVO> allVideos = new ArrayList<LocationVO>();

        Cursor cursor = database.query(
                SQLiteHelper.TABLE_SAVED_LOCATIONS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LocationVO video = cursorToVideo(cursor);
            allVideos.add(video);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return allVideos;
    }
}

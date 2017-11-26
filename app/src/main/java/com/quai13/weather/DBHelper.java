package com.quai13.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by jonathan on 18/10/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "weather.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + DBHelperContract.CityEntry.TABLE_NAME +
            " (" +
            DBHelperContract.CityEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBHelperContract.CityEntry.COLUMN_NAME + " TEXT, " +
            DBHelperContract.CityEntry.COLUMN_COUNTRY + " TEXT, " +
            DBHelperContract.CityEntry.COLUMN_WIND_SPEED + " INTEGER, " +
            DBHelperContract.CityEntry.COLUMN_WIND_ORIENTATION + " TEXT, " +
            DBHelperContract.CityEntry.COLUMN_PRESSURE + " INTEGER, " +
            DBHelperContract.CityEntry.COLUMN_TEMPERATURE + " INTEGER, " +
            DBHelperContract.CityEntry.COLUMN_DATE + " TEXT, " +
            " CONSTRAINT unique_name_country UNIQUE (" + DBHelperContract.CityEntry.COLUMN_NAME + ", " + DBHelperContract.CityEntry.COLUMN_COUNTRY + ") ON CONFLICT FAIL);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBHelperContract.CityEntry.TABLE_NAME;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public Cursor getAllCities(String sortOrder) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DBHelperContract.CityEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder
        );

        return cursor;
    }


    public Cursor getCity(String city, String country) {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = new String[]{
                DBHelperContract.CityEntry._ID,
                DBHelperContract.CityEntry.COLUMN_NAME,
                DBHelperContract.CityEntry.COLUMN_COUNTRY,
                DBHelperContract.CityEntry.COLUMN_TEMPERATURE,
                DBHelperContract.CityEntry.COLUMN_WIND_SPEED,
                DBHelperContract.CityEntry.COLUMN_WIND_ORIENTATION,
                DBHelperContract.CityEntry.COLUMN_DATE,
                DBHelperContract.CityEntry.COLUMN_PRESSURE
        };

        Cursor cursor = db.query(
                DBHelperContract.CityEntry.TABLE_NAME,
                projection,
                DBHelperContract.CityEntry.COLUMN_NAME + "=? AND " + DBHelperContract.CityEntry.COLUMN_COUNTRY + "=?",
                new String[]{city, country},
                null,
                null,
                null,
                "1"
        );

        return cursor;
    }


    public long insertCity(ContentValues values) {

        SQLiteDatabase db = this.getWritableDatabase();

        long insert =  db.insert(DBHelperContract.CityEntry.TABLE_NAME, null, values);

        Log.d("weather", "Insert: " + String.valueOf(insert));


        db.close();

        return insert;
    }


    public long deleteCity(String name, String country) {

        SQLiteDatabase db = this.getWritableDatabase();

        long delete = db.delete(DBHelperContract.CityEntry.TABLE_NAME,
                DBHelperContract.CityEntry.COLUMN_NAME + "=? AND " + DBHelperContract.CityEntry.COLUMN_COUNTRY + "=?",
                new String[] { name, country }
        );

        db.close();

        Log.d("weather", "Delete: " + String.valueOf(delete));

        return delete;
    }


    public long updateCity(ContentValues values) {

        SQLiteDatabase db = this.getWritableDatabase();

        String name = values.getAsString(DBHelperContract.CityEntry.COLUMN_NAME);
        String country = values.getAsString(DBHelperContract.CityEntry.COLUMN_COUNTRY);

        long update = db.update(
                DBHelperContract.CityEntry.TABLE_NAME,
                values,
                DBHelperContract.CityEntry.COLUMN_NAME + "=? AND " + DBHelperContract.CityEntry.COLUMN_COUNTRY + "=?",
                new String[] { name, country }
        );

        db.close();

        Log.d("update at:", name + "," + country);
        Log.d("weather", "Update: " + String.valueOf(update));

        return update;
    }



    public static final class DBHelperContract {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private DBHelperContract() {}

        /* Inner class that defines the table contents */
        public static class CityEntry implements BaseColumns {
            public static final String TABLE_NAME = "cities";
            public static final String COLUMN_NAME = "name";
            public static final String COLUMN_COUNTRY = "country";
            public static final String COLUMN_TEMPERATURE = "temperature";
            public static final String COLUMN_WIND_SPEED = "wind_speed";
            public static final String COLUMN_WIND_ORIENTATION = "wind_orientation";
            public static final String COLUMN_PRESSURE = "pressure";
            public static final String COLUMN_DATE = "date";
        }
    }


}
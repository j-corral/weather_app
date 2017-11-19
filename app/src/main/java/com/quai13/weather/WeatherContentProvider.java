package com.quai13.weather;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jonathan on 23/10/17.
 */

public class WeatherContentProvider extends ContentProvider {

    static final String AUTHORITY = "com.quai13.weather";
    static final String URL = "content://" + AUTHORITY + "/weather";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
            .authority(WeatherContentProvider.AUTHORITY)
            .appendEncodedPath(DBHelper.DBHelperContract.CityEntry.TABLE_NAME)
            .build();

    static final int WEATHER = 1;
    static final int WEATHER_CITY = 2;

    private DBHelper db = null;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, DBHelper.DBHelperContract.CityEntry.TABLE_NAME, WEATHER);
        uriMatcher.addURI(AUTHORITY, DBHelper.DBHelperContract.CityEntry.TABLE_NAME + "/*/*", WEATHER_CITY);
    }


    public static Uri buildURI(String city, String country) {
        return WeatherContentProvider.CONTENT_URI.buildUpon().appendPath(country).appendPath(city).build();
    }

    @Override
    public boolean onCreate() {
        db = new DBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor result = null;

        int item = uriMatcher.match(uri);

        switch (item) {
            case WEATHER:
                result = db.getAllCities(sortOrder);
                break;
            case WEATHER_CITY:
                String city = uri.getPathSegments().get(1);
                String country= uri.getPathSegments().get(2);
                result = db.getCity(city, country);
                break;
            default:
                return result;
        }

        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        String result = null;

        String vnd = ".vnd." + AUTHORITY + ".weather";

        int item = uriMatcher.match(uri);

        switch (item) {
            case WEATHER:
                result = ContentResolver.CURSOR_DIR_BASE_TYPE + vnd;
                break;
            case WEATHER_CITY:
                result = ContentResolver.CURSOR_ITEM_BASE_TYPE + vnd;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }


        return result;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

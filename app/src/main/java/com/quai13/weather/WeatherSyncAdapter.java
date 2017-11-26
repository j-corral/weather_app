package com.quai13.weather;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by jonathan on 21/11/17.
 */

public class WeatherSyncAdapter extends AbstractThreadedSyncAdapter {
    public WeatherSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        try {

            Log.d("sync", "update weather");

            Cursor cities = provider.query(
                    WeatherContentProvider.CONTENT_URI,
                    null,
                    null,
                    null,
                    DBHelper.DBHelperContract.CityEntry.COLUMN_NAME + " ASC"
            );


            if(cities != null) {

                while (cities.moveToNext()) {

                    String name = cities.getString(cities.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_NAME));
                    String country = cities.getString(cities.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_COUNTRY));

                    String adr = YQLBuilder.build(name, country);
                    Log.d("adr", adr);

                    URL url = null;
                    InputStream in = null;

                    url = new URL(adr);

                    URLConnection urlConnection = url.openConnection();
                    in = new BufferedInputStream(urlConnection.getInputStream());

                    JSONResponseHandler JRH = new JSONResponseHandler();
                    List<String> data = JRH.handleResponse(in, "UTF-8");


                    if (data.toArray().length >= 3) {

                        ContentValues values = new ContentValues();

                        Log.d("data [wind]:", data.get(0));
                        Log.d("data [temp]:", data.get(1));
                        Log.d("data [pressure]:", data.get(2));
                        Log.d("data [date]:", data.get(3));

                        String[] wind = data.get(0).split("\\s");

                        values.put(DBHelper.DBHelperContract.CityEntry.COLUMN_NAME, name);
                        values.put(DBHelper.DBHelperContract.CityEntry.COLUMN_COUNTRY, country);
                        values.put(DBHelper.DBHelperContract.CityEntry.COLUMN_WIND_SPEED, wind[0]);
                        values.put(DBHelper.DBHelperContract.CityEntry.COLUMN_WIND_ORIENTATION, wind[1]);
                        values.put(DBHelper.DBHelperContract.CityEntry.COLUMN_TEMPERATURE, data.get(1));
                        values.put(DBHelper.DBHelperContract.CityEntry.COLUMN_PRESSURE, data.get(2));
                        values.put(DBHelper.DBHelperContract.CityEntry.COLUMN_DATE, data.get(3));

                        provider.update(
                                WeatherContentProvider.buildURI(name, country),
                                values,
                                null,
                                null
                        );

                    }

                    in.close();

                }

                cities.close();

            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

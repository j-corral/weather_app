package com.quai13.weather;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.net.URI;

public class CityView extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    City city = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_view);


        city = (City) getIntent().getExtras().getSerializable("city");

        TextView valueCity = (TextView) findViewById(R.id.valueCity);
        valueCity.setText(city.getName());

        TextView valueCountry = (TextView) findViewById(R.id.valueCountry);
        valueCountry.setText(city.getCountry());


        TextView valueWind = (TextView) findViewById(R.id.valueWind);
        valueWind.setText(city.getHWind());

        TextView valueTemp = (TextView) findViewById(R.id.valueTemp);
        valueTemp.setText(city.getHTemp());

        TextView valuePressure = (TextView) findViewById(R.id.valuePressure);
        valuePressure.setText(city.getHPressure());

        TextView valueDate = (TextView) findViewById(R.id.valueDate);
        valueDate.setText(city.getHDate());


        getLoaderManager().initLoader(0, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri uri = WeatherContentProvider.buildURI(city.getName(), city.getCountry());

        Log.d("loader URI: ", uri.toString());

        CursorLoader loader = new CursorLoader(
                this,
                uri,
                null,
                null,
                null,
                DBHelper.DBHelperContract.CityEntry.COLUMN_NAME + " ASC"
        );

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        try {

            if(data != null && data.moveToFirst()) {

                String name = data.getString(data.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_NAME));
                String country = data.getString(data.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_COUNTRY));

                Integer pressure = data.getInt(data.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_PRESSURE));
                city.setPressure(pressure);

                Integer temperature = data.getInt(data.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_TEMPERATURE));
                city.setTemperature(temperature);

                Integer speed = data.getInt(data.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_WIND_SPEED));
                city.setWindSpeed(speed);

                String orientation = data.getString(data.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_WIND_ORIENTATION));
                city.setWindOrientation(orientation);


                String date = data.getString(data.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_DATE));
                city.setDate(date);


                data.close();

            } else {
                Log.d("loader", "data is null");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

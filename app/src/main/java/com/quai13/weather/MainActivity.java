package com.quai13.weather;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private DBHelper db = new DBHelper(this);

    ListView simpleList;

    static final int ADD_NEW_CITY = 1;

    SimpleCursorAdapter adapter;

    Account account = new Account("default", YQLBuilder.host);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            simpleList = (ListView)findViewById(R.id.simpleListView);

            String[] from = { DBHelper.DBHelperContract.CityEntry.COLUMN_NAME, DBHelper.DBHelperContract.CityEntry.COLUMN_COUNTRY };
            int[] to = { R.id.name_entry, R.id.country_entry };
            adapter = new SimpleCursorAdapter(this, R.layout.activity_listview, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

            getLoaderManager().initLoader(0, null, this);

            simpleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view,
                                               final int position, long arg3) {


                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            MainActivity.this);
                    alert.setTitle("Delete");

                    alert.setMessage("city will be removed.");
                    alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Cursor cursor = ((SimpleCursorAdapter) simpleList.getAdapter()).getCursor();
                            cursor.moveToPosition(position);

                            String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_NAME));
                            String country = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_COUNTRY));

                            Uri uri = WeatherContentProvider.buildURI(name, country);

                            int delete = getContentResolver().delete(uri, null, null);

                            if(delete > 0) {
                                getLoaderManager().restartLoader(0, null, MainActivity.this);
                                dialog.dismiss();
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), name + " could not be deleted !", Toast.LENGTH_SHORT);
                                toast.show();
                            }



                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();



                    return true;
                }

            });



            simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Cursor cursor = ((SimpleCursorAdapter) simpleList.getAdapter()).getCursor();
                    cursor.moveToPosition(position);

                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_NAME));
                    String country = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_COUNTRY));
                    City city = new City(name, country);

                    Integer pressure = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_PRESSURE));
                    city.setPressure(pressure);

                    Integer temperature = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_TEMPERATURE));
                    city.setTemperature(temperature);

                    Integer speed = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_WIND_SPEED));
                    city.setWindSpeed(speed);

                    String orientation = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_WIND_ORIENTATION));
                    city.setWindOrientation(orientation);


                    String date = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DBHelperContract.CityEntry.COLUMN_DATE));
                    city.setDate(date);

                    Intent intent = new Intent(MainActivity.this, CityView.class);
                    intent.putExtra("city", city);
                    startActivity(intent);
                }
            });

            simpleList.setAdapter(adapter);

            ContentResolver.removePeriodicSync(
                    account,
                    WeatherContentProvider.AUTHORITY,
                    Bundle.EMPTY
            );

            ContentResolver.setSyncAutomatically(
                    account,
                    WeatherContentProvider.AUTHORITY,
                    true
            );

            ContentResolver.addPeriodicSync(
                    account,
                    WeatherContentProvider.AUTHORITY,
                    Bundle.EMPTY,
                    60L * 60L
            );


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.addCity:

                Intent intent = new Intent(this, AddCityActivity.class);
                startActivityForResult(intent, ADD_NEW_CITY);
                return true;
            case R.id.refreshWeather:

                try{
                    updateWeather();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NEW_CITY) {
            if (resultCode == RESULT_OK) {

                String name = data.getStringExtra("city");
                String country = data.getStringExtra("country");

                Uri uri = WeatherContentProvider.buildURI(name, country);

                ContentValues values = new ContentValues();
                values.put(DBHelper.DBHelperContract.CityEntry.COLUMN_NAME, name);
                values.put(DBHelper.DBHelperContract.CityEntry.COLUMN_COUNTRY, country);

                Uri insert = getContentResolver().insert(uri, values);

                if(insert == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), name + " could not be inserted !", Toast.LENGTH_SHORT);
                    toast.show();
                }

                getLoaderManager().restartLoader(0, null, this);


                try{
                    updateWeather();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private void updateWeather() throws Exception {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(
                account,
                WeatherContentProvider.AUTHORITY,
                settingsBundle
        );

        Toast toast = Toast.makeText(this, "Updating weather...", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader loader = new CursorLoader(
                this,
                WeatherContentProvider.CONTENT_URI,
                null,
                null,
                null,
                DBHelper.DBHelperContract.CityEntry.COLUMN_NAME + " ASC"
        );

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


}

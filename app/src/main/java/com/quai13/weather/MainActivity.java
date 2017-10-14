package com.quai13.weather;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Array of strings...
    ListView simpleList;

    ArrayList<City> cities;

    static final int ADD_NEW_CITY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            // Allow Network Connection
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
                StrictMode.setThreadPolicy(policy);
            }


            cities = new ArrayList<City>();


            populateCities();


            simpleList = (ListView)findViewById(R.id.simpleListView);
            final ArrayAdapter<City> arrayAdapter = new ArrayAdapter<City>(this, R.layout.activity_listview, R.id.textView, cities);


            simpleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               final int position, long arg3) {


                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            MainActivity.this);
                    alert.setTitle("Delete");

                    City city = arrayAdapter.getItem(position);


                    alert.setMessage(city.getName() + " will be removed.");
                    alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cities.remove(position);
                            arrayAdapter.notifyDataSetChanged();
                            dialog.dismiss();
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

                    City city = arrayAdapter.getItem(position);

                    Intent intent = new Intent(MainActivity.this, CityView.class);
                    intent.putExtra("city", city);
                    startActivity(intent);
                }
            });




            simpleList.setAdapter(arrayAdapter);


            updateWeather();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void populateCities() {

        City Marseille = new City("Marseille", "France", 10, "SE", 28, 1024);
        cities.add(Marseille);

        City Avignon = new City("Avignon", "France", 23, "NE", 22, 1105);
        cities.add(Avignon);

        City Seoul = new City("Seoul", "Korea", 34, "NO", 19, 889);
        cities.add(Seoul);
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
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.addCity:

                Intent intent = new Intent(this, AddCityActivity.class);
                startActivityForResult(intent, ADD_NEW_CITY);
                return true;
            case R.id.refreshWeather:
                new RefreshWeatherTask().doInBackground();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NEW_CITY) {
            if (resultCode == RESULT_OK) {

                String city = data.getStringExtra("city");
                String country = data.getStringExtra("country");

                City newCity = new City(city, country);

                cities.add(newCity);

                updateWeather();

            }
        }
    }



    public class RefreshWeatherTask extends AsyncTask<Void, Void, City> {

        @Override
        protected City doInBackground(Void... params) {

            updateWeather();

            return null;
        }
    }


    private void updateWeather() {

        for (int i=0; i < cities.toArray().length; ++i) {

            String adr = YQLBuilder.build(cities.get(i).getName(), cities.get(i).getCountry());

            URL url = null;
            InputStream in = null;

            try {
                url = new URL(adr);

                URLConnection urlConnection = url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());

                JSONResponseHandler JRH = new JSONResponseHandler();
                List<String> data = JRH.handleResponse(in, "UTF-8");


                if(data.toArray().length >= 3) {

                    cities.get(i).setWind(data.get(0));
                    cities.get(i).setTemperature(data.get(1));
                    cities.get(i).setPressure(data.get(2));

                }

                in.close();

                Toast toast = Toast.makeText(getApplicationContext(), "Weather updated.", Toast.LENGTH_LONG);
                toast.show();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }

    }


}

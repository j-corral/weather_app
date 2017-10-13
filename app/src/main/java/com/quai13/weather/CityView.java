package com.quai13.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CityView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_view);


        City city = (City) getIntent().getExtras().getSerializable("city");

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





    }
}

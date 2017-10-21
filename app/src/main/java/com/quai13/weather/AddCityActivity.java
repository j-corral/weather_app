package com.quai13.weather;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCityActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Button btnAddCity = (Button) findViewById(R.id.btnAddCity);

        btnAddCity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText inputCity = (EditText) findViewById(R.id.inputCity);
                EditText inputCountry = (EditText) findViewById(R.id.inputCountry);

                String city = inputCity.getText().toString();
                String country = inputCountry.getText().toString();

                Intent intent = new Intent();

                if(city.isEmpty() || country.isEmpty()) {

                    Toast alert = Toast.makeText(getApplicationContext(), "You must fill all the fields !", Toast.LENGTH_SHORT);
                    alert.setGravity(Gravity.CENTER, 0,0);
                    alert.show();

                } else {

                    /*Toast alert = Toast.makeText(getApplicationContext(), "City " + city +" added successfully", Toast.LENGTH_LONG);
                    alert.setGravity(Gravity.CENTER, 0,0);
                    alert.show();*/

                    intent.putExtra("city", city);
                    intent.putExtra("country", country);
                    setResult(RESULT_OK, intent);
                    finish();
                }




            }
        });



    }

}

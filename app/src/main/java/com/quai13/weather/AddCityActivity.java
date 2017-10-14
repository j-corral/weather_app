package com.quai13.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddCityActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Button btnAddCity = (Button) findViewById(R.id.btnAddCity);

        btnAddCity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText inputCity = (EditText) findViewById(R.id.inputCity);
                EditText inputCountry = (EditText) findViewById(R.id.inputCountry);

                Intent intent = new Intent();
                intent.putExtra("city", inputCity.getText().toString());
                intent.putExtra("country", inputCountry.getText().toString());
                setResult(RESULT_OK, intent);
                finish();




            }
        });



    }

}

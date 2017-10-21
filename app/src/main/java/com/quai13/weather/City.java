package com.quai13.weather;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jonathan on 09/10/17.
 */

public class City implements Serializable {

    private int id;
    private String name;
    private String country;
    private Integer wind_speed = 0;
    private String wind_orientation = "";
    private Integer temp = 0;
    private Integer pressure = 0;
    private String date = "";
    private String format = "dd/M/yyyy H:m";

    public City(String name, String country) {
        this.name = Tools.ucFirst(name);
        this.country = Tools.ucFirst(country);
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public int getId() {
        return id;
    }

    public Integer getWindSpeed() {
        return wind_speed;
    }

    public String getWindOrientation() {
        return wind_orientation;
    }

    public Integer getTemperature() {
        return temp;
    }

    public Integer getPressure() {
        return pressure;
    }

    public String getDate() {
        return date;
    }

    public String getHWind() {
        return wind_speed  + " km/h " + wind_orientation  + "";
    }

    public String getHTemp() {
        return temp + " °C";
    }

    public String getHPressure() {
        return pressure + " hPa";
    }

    public String getHDate() {

        //SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(format);
        //return DATE_FORMAT.format(this.date);

        return date;
    }


    public void setWind(String wind) {
        String[] Wind = wind.split("\\s");

        if(Wind.length > 1) {
            this.wind_speed = Integer.valueOf(Wind[0]);
            this.wind_orientation = Wind[1];
        }

    }


    public void setWindSpeed(int wind_speed) {
        this.wind_speed = wind_speed;
    }

    public void setWindOrientation(String wind_orientation) {
        this.wind_orientation = wind_orientation;
    }


    public void setTemperature(int temp) {
        this.temp = temp;
    }

    public void setTemperature(String temp) {
        this.temp = Integer.valueOf(temp);
    }

    public void setPressure(String pressure) {
        Double Pressure = Double.parseDouble(pressure);
        this.pressure = Pressure.intValue();
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public void setDate(String date) {

        this.date = date;

        /*SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.FRANCE);

        try {

            date = date.replace("\u00a0","").trim();

            Log.d("date", date);

            this.date = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public String toString() {
        return name + " (" + country + ")";
    }
}

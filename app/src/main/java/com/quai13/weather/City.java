package com.quai13.weather;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jonathan on 09/10/17.
 */

public class City implements Serializable {

    private String name;
    private String country;
    private Date update;
    private Integer wind_speed;
    private String wind_orientation;
    private Integer pressure;
    private Integer temp;
    private String format = "dd/M/yyyy H:m";

    public City(String name, String country) {
        this.name = Tools.ucFirst(name);
        this.country = Tools.ucFirst(country);
        setUpdate();
    }

    public City(String name, String country, int wind_speed, String wind_orientation, int temp, int pressure) {
        this.name = name;
        this.country = country;
        this.wind_speed = wind_speed;
        this.wind_orientation = wind_orientation;
        this.temp = temp;
        this.pressure = pressure;
        setUpdate();
    }


    private void setUpdate() {
        this.update = new Date();
    }

    public String getName() {
        return name;
    }


    public String getCountry() {
        return country;
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
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(format);

        return DATE_FORMAT.format(update);
    }


    public void setWind(String wind) {
        String[] Wind = wind.split("\\s");

        if(Wind.length > 1) {
            this.wind_speed = Integer.valueOf(Wind[0]);
            this.wind_orientation = Wind[1];
        }

    }


    public void setTemperature(String temp) {
        this.temp = Integer.valueOf(temp);
    }


    public void setPressure(String pressure) {
        Double Pressure = Double.parseDouble(pressure);
        this.pressure = Pressure.intValue();
    }


    public void setDate(String date) {

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        try {
            this.update = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return name + " (" + country + ")";
    }
}

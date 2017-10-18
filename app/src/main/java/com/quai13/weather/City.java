package com.quai13.weather;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private Date date = new Date();
    private String format = "dd/M/yyyy H:mm";

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

    public Date getDate() {
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
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(format);

        return DATE_FORMAT.format(this.date);
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

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        /*try {
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

package com.quai13.weather;

import android.net.Uri;
import android.util.Log;

/**
 * Created by jonathan on 14/10/17.
 */

/**
 * Build URL from Yahoo API matching to given city and country
 */
public class YQLBuilder {

    private static String scheme = "https";
    public static final String host = "query.yahooapis.com";
    private static String path = "v1/public/yql";
    private static String queryParam = "q";
    private static String format = "json";

    public static String build(String city, String country) {
        return build(city, country, "f");
    }


    /**
     * Build API url
     * @param city
     * @param country
     * @param units
     * @return string url
     */
    public static String build(String city, String country, String units) {

        String request = "select title, location, units, wind, atmosphere, item.pubDate, item.condition " +
                "from weather.forecast " +
                "where woeid in (select woeid " +
                    "from geo.places(1) " +
                    "where text=\""+city.toLowerCase()+", "+ country.toLowerCase()+"\") " +
                "and u=\""+units+"\" ";


        return encodeURL(request);
    }


    private static String encodeURL(String request) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(scheme);
        builder.authority(host);
        builder.path(path);
        builder.appendQueryParameter(queryParam, request);
        builder.appendQueryParameter("format", format);

        return builder.build().toString();
    }


}

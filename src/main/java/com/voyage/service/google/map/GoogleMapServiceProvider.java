package com.voyage.service.google.map;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.voyage.service.UrlFetchService;

public class GoogleMapServiceProvider {

    public GoogleMapQueryResult getGeocode(String address) {
        URL url;
        GoogleMapQueryResult r = null;
        try {
            url = new URL("http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address="
                    + URLEncoder.encode(address, "UTF-8"));
            StringBuilder result = new UrlFetchService().fetch(url);
            StringReader sr = new StringReader(result.toString()); // wrap your
            r = new Gson().fromJson(new BufferedReader(sr), GoogleMapQueryResult.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

}

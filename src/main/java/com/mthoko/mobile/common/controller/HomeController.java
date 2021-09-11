package com.mthoko.mobile.common.controller;

import com.mthoko.mobile.common.util.DataManager;
import com.mthoko.mobile.domain.address.Address;
import com.mthoko.mobile.domain.locationstamp.LocationStamp;
import com.mthoko.mobile.domain.locationstamp.LocationStampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private LocationStampService service;

    @RequestMapping("address/{imei}")
    public String addressByImeiFromLatlng(Map<String, Object> model, @PathVariable("imei") String imei) {
        try {
            ArrayList<String> output = new ArrayList<String>();
            Optional<LocationStamp> optionalLocationStamp = service.findMostRecentByImei(imei);
            if (optionalLocationStamp.isPresent()) {
                LocationStamp locationStamp = optionalLocationStamp.get();
                double latitude = Double.parseDouble(locationStamp.getLatitude());
                double longitude = Double.parseDouble(locationStamp.getLongitude());
                Address address = DataManager.retrieveAddress(latitude, longitude);
                output.add("Device: " + locationStamp.getDeviceRequested());
                output.add("Imei: " + locationStamp.getImei());
                Date timeCaptured = locationStamp.getTimeCaptured();
                output.add("Time: " + timeCaptured);
                output.add("Country: " + address.getCountry());
                output.add("State: " + address.getState());
                output.add("City: " + address.getCity());
                output.add("PostalCode: " + address.getPostalCode());
                output.add("Street: " + address.getStreet());
                output.add("Latitude: " + latitude);
                output.add("Longitude: " + longitude);
                model.put("records", output);
                model.put("title", "Location Trace");
                model.put("link", getGoogleMapsLink(latitude, longitude));
            }
            return "db";
        } catch (Exception e) {
            model.put("message", e.getMessage());
            return "error";
        }
    }

    public String getGoogleMapsLink(Double latitude, Double longitude) {
        return String.format("https://www.google.co.za/maps/search/%s,%s", latitude, longitude);
    }

    @RequestMapping("latlng")
    public String addressFromLatlng(Map<String, Object> model, @RequestParam("latitude") Double latitude,
                                    @RequestParam("longitude") Double longitude) {
        try {
            ArrayList<String> output = new ArrayList<String>();
            Address address = DataManager.retrieveAddress(latitude, longitude);
            output.add("Time: " + new Date(new Date().getTime() + 2 * (1000 * 60 * 60)));
            output.add("Country: " + address.getCountry());
            output.add("State: " + address.getState());
            output.add("City: " + address.getCity());
            output.add("PostalCode: " + address.getPostalCode());
            output.add("Street: " + address.getStreet());
            output.add("Latitude: " + latitude);
            output.add("Longitude: " + longitude);
            model.put("records", output);
            model.put("title", "Location Trace");
            model.put("link", getGoogleMapsLink(latitude, longitude));
            return "db";
        } catch (Exception e) {
            model.put("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping
    public String index() {
        return "index";
    }

    @RequestMapping("time")
    @ResponseBody
    public Date getTime() {
        long twoHours = 2 * 60 * 60 * 1000;
        return new Date(new Date().getTime() + twoHours);
    }

    @RequestMapping("app/property/name/{name}")
    @ResponseBody
    public String getAppProperty(@PathVariable("name") String name) {
        return service.getAppProperty(name);
    }


}

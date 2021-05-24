/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author alexi
 */
import geocode.GeocodeResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URLEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class GeocodeController {
    
    // This method is just for Postman request to check the data without needing the map
    @RequestMapping(path = "/geocode", method = RequestMethod.GET )
    public GeocodeResult getGeocode(@RequestParam String address) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String encodedAddress = URLEncoder.encode(address, "UTF-8");
        Request request = new Request.Builder()
                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?language=en&address=" + encodedAddress)
                .get()
                .addHeader("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "5e1489ac01msh052a7782cf44e6fp196e82jsn628a2bdab023"/*  Use your API Key here */)
                .build();
        ResponseBody responseBody = client.newCall(request).execute().body();
        ObjectMapper objectMapper = new ObjectMapper();
        GeocodeResult result = objectMapper.readValue(responseBody.string(), GeocodeResult.class);
        return result;
    }
    
      // HTML page using ModelAndView     
//    @RequestMapping(path = "/geocodeMap", method = RequestMethod.GET )
//    public ModelAndView getGeocodeMap(@RequestParam String address) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//        String encodedAddress = URLEncoder.encode(address, "UTF-8");
//        Request request = new Request.Builder()
//                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?language=en&address=" + encodedAddress)
//                .get()
//                .addHeader("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", "5e1489ac01msh052a7782cf44e6fp196e82jsn628a2bdab023"/*  Use your API Key here */)
//                .build();
//        ResponseBody responseBody = client.newCall(request).execute().body();
//        ObjectMapper objectMapper = new ObjectMapper();
//        GeocodeResult result = objectMapper.readValue(responseBody.string(), GeocodeResult.class);
//
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("getAddress", result.getResults().get(0).getFormattedAddress());
//        mav.addObject("getLatitude", result.getResults().get(0).getGeometry().getGeocodeLocation().getLatitude());
//        mav.addObject("getLongitude", result.getResults().get(0).getGeometry().getGeocodeLocation().getLongitude());
//        mav.setViewName("map");
//        return mav;
//    }
    
}
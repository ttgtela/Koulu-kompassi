package com.jmnt.controllers;

import com.jmnt.data.Coord;
import com.jmnt.data.NominatimApiCaller;
import com.jmnt.data.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class NominatimApiController {
    private final NominatimApiCaller nominatimApiCaller;

    @Autowired
    public NominatimApiController(NominatimApiCaller nominatimApiCaller) {
        this.nominatimApiCaller = nominatimApiCaller;
    }

    @GetMapping("/searchall")
    public Map<String, Coord> searchAllCoordinates(List<String> schoolNames) {
        Map<String, Coord> coordMap = new HashMap<>();
        for (String schoolName : schoolNames) {
            Place[] result = nominatimApiCaller.searchLocation(schoolName);

            for (Place place : result) {
                if (Objects.equals(place.getAdresstype(), "amenity")) {
                    Coord coord = new Coord(place.getLat(), place.getLon());
                    coordMap.put(place.getName(), coord);
                }
            }
        }

        return coordMap;
    }
}

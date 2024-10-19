package com.jmnt.controllers;

import com.jmnt.data.Coord;
import com.jmnt.data.NominatimApiCaller;
import com.jmnt.data.Place;
import com.jmnt.tools.UniTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public Map<String, Coord> searchAllCoordinates(List<String> schoolNames) {
        Map<String, Coord> coordMap = new HashMap<>();
        for (String schoolName : schoolNames) {
            System.out.println("schoolName: " + schoolName);
            Place[] result = nominatimApiCaller.searchLocation(schoolName);

            for (Place place : result) {
                System.out.println(place.getName());
                System.out.println(place.getAddresstype());
                if (place.getAddresstype().equals("amenity")) {
                    Coord coord = new Coord(place.getLat(), place.getLon());
                    coordMap.put(place.getName(), coord);
                }
            }
        }
        return coordMap;
    }


    @GetMapping("/coordinates")
    public Map<String, Coord> getCoordinates() {
        return searchAllCoordinates(UniTools.getUniversityNames());
    }
    @GetMapping("/names")
    public List<String> getNames() {
        return UniTools.getUniversityNames();
    }

    @GetMapping("/search")
    public Place[] search() {
        return nominatimApiCaller.searchLocation("Hämeenlinna");
    }
}

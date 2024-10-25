package com.jmnt.controllers;

import com.jmnt.data.*;
import com.jmnt.tools.UniTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    public Map<String, Map<String, Coord>> searchAllCoordinates(List<String> schoolNames) {
        Map<String, Map<String, Coord>> coordMap = new HashMap<>();

        CoordinateCache coordinateCache = new CoordinateCache();
        Map<String, Map<String, Coord>> cache = coordinateCache.getCache();

        for (String schoolName : schoolNames) {

            if (cache != null) {
                if (cache.containsKey(schoolName)) {
                    //coordMap.putAll(cache.get(schoolName));
                    coordMap.put(schoolName, cache.get(schoolName));
                    continue;
                }
            }

            Place[] result = nominatimApiCaller.searchLocation(schoolName);
            if (result.length == 0) {
                continue;
            }
            cache.put(schoolName, new HashMap<>());

            for (Place place : result) {

                String[] parts = place.getDisplay_name().split(", ");
                if (place.getAddresstype().equals("amenity") && parts[parts.length-1].equals("Suomi / Finland")) {
                    Coord coord = new Coord(place.getLat(), place.getLon());
                    Map<String, Coord> innerMap = new HashMap<>();
                    innerMap.put(place.getName(), coord);
                    coordMap.put(schoolName, innerMap);

                    cache.get(schoolName).put(place.getName(), coord);
                }
            }
        }
        coordinateCache.saveCache();
        System.out.println("Coordinates searched");
        return coordMap;
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/api/unicoordinates")
    public Map<String, Map<String, Coord>> getUniCoordinates() {
        return searchAllCoordinates(UniTools.getUniversityNames());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/api/hscoordinates")
    public Map<String, Map<String, Coord>> getHsCoordinates() {
        return searchAllCoordinates(UniTools.fetchHsNames());
    }
    /**
    @GetMapping("/api/numberofcampuses")
    public Map<String, Integer> getCampuses() {
        Map<String, Integer> campuses = new HashMap<>();
        ExamResultCaller examResultCaller = new ExamResultCaller();
        ExamResultController examResultController = new ExamResultController(examResultCaller);
        ArrayList<String> schoolNames = examResultController.getSchools();

        for (String schoolName : schoolNames) {
            Place[] result = nominatimApiCaller.searchLocation(schoolName);
            for (Place place : result) {
                if (place.getAddresstype().equals("amenity")){
                    campuses.put(place.getName(), campuses.getOrDefault(place.getName(), 0) + 1);
                }
            }
        }

        return campuses;
    }
     **/

    @GetMapping("/names")
    public List<String> getNames() {
        return UniTools.getUniversityNames();
    }

    @GetMapping("/hsnames")
    public List<String> getHsNames() {
        return UniTools.fetchHsNames();
    }
}

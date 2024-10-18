package com.jmnt.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NominatimApiController {
    private final NominatimApiCaller nominatimApiCaller;

    @Autowired
    public NominatimApiController(NominatimApiCaller nominatimApiCaller) {
        this.nominatimApiCaller = nominatimApiCaller;
    }

    @GetMapping("/search")
    public ResponseEntity<Place[]> searchLocation(@RequestParam String query) {
        Place[] result = nominatimApiCaller.searchLocation(query);
        if (result.length == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }
}

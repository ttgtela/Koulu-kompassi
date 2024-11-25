package com.jmnt.controllers;

import com.jmnt.data.CombinedUniversityData;
import com.jmnt.services.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CombinedDataController {

    private final UniversityService universityService;

    @Autowired
    public CombinedDataController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping("/api/combined")
    public ResponseEntity<List<CombinedUniversityData>> getCombinedData() {
        try {
            List<CombinedUniversityData> combinedData = universityService.getCombinedData();
            return ResponseEntity.ok(combinedData);
        } catch (Exception e) {
            System.out.println(e);
            List<CombinedUniversityData> combinedData = new ArrayList<>();
            CombinedUniversityData combinedUniversityData = new CombinedUniversityData();
            combinedUniversityData.setUniversityName("Error university!");
            combinedData.add(combinedUniversityData);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(combinedData);
        }
    }
}

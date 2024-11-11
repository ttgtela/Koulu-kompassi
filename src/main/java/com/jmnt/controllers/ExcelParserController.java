package com.jmnt.controllers;

import com.jmnt.data.University;
import com.jmnt.data.ParsedUniversityContext;
import com.jmnt.tools.UniTools;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/excel")
public class ExcelParserController {

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/universities")
    public ResponseEntity<List<University>> getUniversities() {
        try {
            ParsedUniversityContext data = ParsedUniversityContext.getInstance();
            return ResponseEntity.ok(data.getUniversities().get(2024));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/university/{name}")
    public ResponseEntity<University> getUniversity(@PathVariable String name) {
        try {
            ParsedUniversityContext data = ParsedUniversityContext.getInstance();
            for (University university:data.getUniversities().get(2024)){
                if (university.getName().equals(name)){
                    return ResponseEntity.ok(university);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "University not found");
    }
    public ResponseEntity<University> getUniversityDataForYear(@PathVariable String name,int year) {
        try {
            ParsedUniversityContext data = ParsedUniversityContext.getInstance();
            for (University university:data.getUniversities().get(year)){
                if (university.getName().equals(name)){
                    return ResponseEntity.ok(university);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "University not found");
    }

    @CrossOrigin(origins = "http://localhost:5173/home")
    @GetMapping("/graph/{name}/{field}/{admissionMethod}")
    public ResponseEntity<Map<Integer,University.AdmissionMethod>> getGraph(@PathVariable String name
            ,@PathVariable String field,@PathVariable String admissionMethod) {
        Map<Integer,University> universityMap = new HashMap<>(Map.of());
        for (int yr=2020;yr<2025;yr++){
            universityMap.put(yr,getUniversityDataForYear(name,yr).getBody());
        }
        Map<Integer,University.AdmissionMethod> admissionMap = new HashMap<>(Map.of());
        for (Integer year:universityMap.keySet()){
            University yearData=universityMap.get(year);
            for (University.FieldOfStudy fi:yearData.getFields()){
                if (fi.getName().equals(field)){
                    for (University.AdmissionMethod am:fi.getAdmissionMethods()){
                        if (am.getName().equals(admissionMethod)){
                            admissionMap.put(year,am);
                        }
                    }
                }
            }
        }
        return ResponseEntity.ok(admissionMap);
    }
}

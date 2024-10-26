package com.jmnt.controllers;

import com.jmnt.data.University;
import com.jmnt.data.ParsedUniversityContext;
import com.jmnt.tools.UniTools;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
}

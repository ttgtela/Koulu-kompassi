package com.jmnt.controllers;

import com.jmnt.excelparser.ExcelParser;
import com.jmnt.data.University;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
public class ExcelParserController {

    @GetMapping("/universities")
    public ResponseEntity<Map<Integer,List<University>>> getUniversities() {
        try {
            Map<Integer,List<University>> universities = ExcelParser.parse();
            return ResponseEntity.ok(universities);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

package com.jmnt.controllers;

import com.jmnt.data.*;
import com.jmnt.tools.WebScraper;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UniversityRequirementsController {

    public List<UniversityPoints> getUniversityPoints() {
        List<UniversityPoints> universityPoints = new ArrayList<>();
        //PointsCache pointsCache = new PointsCache();
        //Map<String, Map<String, UniversityPoints>> cache = pointsCache.getCache();

        Document doc = null;
        try {
            doc = WebScraper.fetchDocument("https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/biokemia-biologia-ja-ymparistotieteet-seka-biolaaketiede");
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        List<String> headers = WebScraper.getMainFieldNames(doc);
        List<UniversityProgram> program_data = WebScraper.getUniversityProgramData(doc);
        List<SubjectPoints> subject_points = WebScraper.getSubjectPointsData(doc);

        for(UniversityProgram data : program_data) {
            System.out.println(data.toString());
        }
        System.out.println("program_data size " + program_data.size());
        System.out.println("subject_points size " + subject_points.size());
        List<UniversityProgram> program = new ArrayList<>();
        List<SubjectPoints> points = new ArrayList<>();


        for(int i = 0; i < headers.size(); i++) {
            for (UniversityProgram programData : program_data) {
                if (programData.getTableIndex() == i) {
                    program.add(programData);
                }
            }
            for (SubjectPoints subjectPoint : subject_points) {
                if (subjectPoint.getTableIndex() == i) {
                    points.add(subjectPoint);
                }
            }
            UniversityPoints universityProgram = new UniversityPoints(headers.get(i), program, points, i);
            universityPoints.add(universityProgram);
        }

        return universityPoints;
    }

    @GetMapping("/points")
    public ResponseEntity<List<UniversityPoints>> GetPointsData() {
        try {
            var data = getUniversityPoints();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

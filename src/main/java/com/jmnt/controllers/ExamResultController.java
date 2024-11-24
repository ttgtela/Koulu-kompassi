package com.jmnt.controllers;

import com.jmnt.data.ExamResultCaller;
import com.jmnt.data.ExamResults;
import com.jmnt.data.SchoolStats;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ExamResultController {

    private final ExamResultCaller caller;
    private final ExamResults[] results;
    private Map<String, SchoolStats> statsMap;

    @Autowired
    public ExamResultController(ExamResultCaller caller) {
        this.caller = caller;
        this.results = caller.searchExams();
        this.statsMap = new HashMap<>();
    }

    private static class CustomGradeComparator implements Comparator<String> {
        private static final List<String> gradeOrder = Arrays.asList("I", "A", "B", "C", "M", "E", "L");

        @Override
        public int compare(String name1, String name2) {
            int index1 = gradeOrder.indexOf(name1);
            int index2 = gradeOrder.indexOf(name2);
            return Integer.compare(index1, index2);
        }
    }
    @CrossOrigin(origins = "http://localhost:5173/home")
    @GetMapping("/api/schools")
    public ArrayList<String> getSchools(){
        ArrayList<String> schools = new ArrayList<>();
        for (ExamResults result : results){
            if (!schools.contains(result.getHighSchool())){
                schools.add(result.getHighSchool());
            }
        }
        return schools;
    }

    public SchoolStats getStats(String school, String year){

        if (statsMap.containsKey(school)){
            return statsMap.get(school);
        }

        ExamResults[] results = caller.searchStats(school, year);
        int numberOfStudents = caller.searchStudents(school, year);
        float numberOfExams = 0;
        float totalGrades = 0;

        Map<String, Integer> grades = new TreeMap<>(new CustomGradeComparator());
        SchoolStats stats = new SchoolStats();

        for (ExamResults result : results){

            grades.merge(result.getGradeCode(), 1, Integer::sum);
            numberOfExams++;
            totalGrades += Integer.parseInt(result.getGradePoints());

        }

        float average = totalGrades / numberOfExams;
        average = Math.round(average * 100) / 100.0f;
        stats.setStudentCount(numberOfStudents);
        stats.setAverageGrade(average);
        stats.setExamGrades(grades);
        statsMap.put(school, stats);
        return stats;
    }

    @CrossOrigin(origins = "http://localhost:5173/home")
    @GetMapping("/api/stats/{school}/{year}/examgrades")
    public ResponseEntity<Map<String, Integer>> getExamGrades(@PathVariable String school, @PathVariable String year){
        SchoolStats stats = getStats(school, year);
        return ResponseEntity.ok(stats.getExamGrades());
    }

    @CrossOrigin(origins = "http://localhost:5173/home")
    @GetMapping("/api/stats/{school}/{year}/studentgrades")
    public ResponseEntity<Pair<Integer, Float>> getStudentGrades(@PathVariable String school, @PathVariable String year){
        SchoolStats stats = getStats(school, year);
        Pair<Integer, Float> pair = new Pair<>(stats.getStudentCount(), stats.getAverageGrade());
        return ResponseEntity.ok(pair);
    }

}

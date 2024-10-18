package com.jmnt.controllers;

import com.jmnt.data.ExamResultCaller;
import com.jmnt.data.ExamResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ExamResultController {

    private ExamResults[] results;

    @Autowired
    public ExamResultController(ExamResultCaller caller) {
        this.results = caller.searchExams();
    }


    @GetMapping("/schools")
    public ArrayList<String> getSchools(){
        ArrayList<String> schools = new ArrayList<>();
        for (ExamResults result : results){
            if (!schools.contains(result.getHighSchool())){
                schools.add(result.getHighSchool());
            }
        }
        return schools;
    }

}

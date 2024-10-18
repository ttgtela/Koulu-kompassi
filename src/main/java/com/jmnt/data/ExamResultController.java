package com.jmnt.data;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ExamResultController {

    private ExamResults[] results;
    private ExamResultCaller caller = new ExamResultCaller();

    private void populateResults(){
        results = caller.searchExams();
    }

    public ArrayList<String> getSchools(){
        if (results == null){
            populateResults();
        }
        ArrayList<String> schools = new ArrayList<>();
        for (ExamResults result : results){
            if (!schools.contains(result.getHighSchool())){
                schools.add(result.getHighSchool());
            }
        }
        return schools;
    }
    
}

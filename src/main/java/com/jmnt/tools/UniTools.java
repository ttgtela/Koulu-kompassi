package com.jmnt.tools;

import com.jmnt.data.ParsedUniversityContext;
import com.jmnt.data.University;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UniTools {
    public static List<String> getUniversityNames(){
        ParsedUniversityContext context = ParsedUniversityContext.getInstance();
        Map<Integer, List<University>> uniMap = context.getUniversities();
        List<String> uniNames = new ArrayList<String>();
        for (University uni : uniMap.get(2024)){
            uniNames.add(uni.getName());
        }

        return uniNames;
    }


}

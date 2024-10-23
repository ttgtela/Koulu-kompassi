package com.jmnt.tools;

import com.jmnt.data.ParsedUniversityContext;
import com.jmnt.data.University;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static String normalizeString(String str){
        str = str.toLowerCase();
        return str.replace('ä', 'a')
                .replace('ö', 'o')
                .replace('å', 'a');
    }

    public static List<String> fetchHsNames() {
        RestTemplate restTemplate = new RestTemplate();
        String[] HsNames = restTemplate.getForObject("http://localhost:8080/api/schools", String[].class);
        if (HsNames != null){
            return Arrays.asList(HsNames);
        } else {
            throw new RuntimeException("No names found");
        }
    }

}

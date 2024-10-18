package com.jmnt.data;
import com.jmnt.excelparser.ExcelParser;

import java.util.List;
import java.util.Map;

public class ParsedUniversityContext {
    private static ParsedUniversityContext instance;

    public Map<Integer, List<University>> getUniversities() {
        return universities;
    }

    private Map<Integer, List<University>> universities;

    private ParsedUniversityContext() {
        universities = ExcelParser.parse();
    };
    public static ParsedUniversityContext getInstance() {
        if (instance == null) {
            instance = new ParsedUniversityContext();
        }
        return instance;
    }
}

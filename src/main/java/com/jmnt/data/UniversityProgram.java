package com.jmnt.data;

import java.util.ArrayList;
import java.util.Map;

public class UniversityProgram {
    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void addGrade(String subject, Character grade) {
        this.requiredGrades.put(subject, grade);
    }

    public UniversityProgram(String program, String university, Map<String, Character> requiredGrades, int tableIndex) {
        this.program = program;
        this.university = university;
        this.requiredGrades = requiredGrades;
        this.tableIndex = tableIndex;
    }

    @Override
    public String toString() {
        return "UniversityProgram{" +
                "program='" + program + '\'' +
                ", university='" + university + '\'' +
                ", requiredGrades=" + requiredGrades +
                ", tableIndex=" + tableIndex +
                '}';
    }

    private String program;
    private String university;

    public int getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    private int tableIndex = 0;

    public Map<String, Character> getRequiredGrades() {
        return requiredGrades;
    }

    public void setRequiredGrades(Map<String, Character> requiredGrades) {
        this.requiredGrades = requiredGrades;
    }

    private Map<String, Character> requiredGrades;
}

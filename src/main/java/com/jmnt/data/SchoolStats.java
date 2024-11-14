package com.jmnt.data;

import java.util.Map;

public class SchoolStats {

    private int studentCount;
    private float averageGrade;
    private Map<String, Integer> examGrades;

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public float getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(float averageGrade) {
        this.averageGrade = averageGrade;
    }

    public Map<String, Integer> getExamGrades() {
        return examGrades;
    }

    public void setExamGrades(Map<String, Integer> examGrades) {
        this.examGrades = examGrades;
    }
}

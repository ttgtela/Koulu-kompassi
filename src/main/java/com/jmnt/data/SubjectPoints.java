package com.jmnt.data;

import java.util.*;
import static com.jmnt.utilities.GradeUtils.GRADES;

public class SubjectPoints {
    public Map<String, Map<Character, Float>> getFieldsPoints() {
        return fieldsPoints;
    }

    private Map<String, Map<Character, Float>> fieldsPoints;

    public int getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    public void setFieldsPoints(Map<String, Map<Character, Float>> fieldsPoints) {
        this.fieldsPoints = fieldsPoints;
    }

    private int tableIndex;

    public void setBestOf(int bestOf) {
        this.bestOf = bestOf;
    }

    private int bestOf;

    public SubjectPoints() {
        this.fieldsPoints = new HashMap<>();
        this.tableIndex = 0;
        this.bestOf = 0;
    }

    public void updateGrade(String subject, Character grade, Float points) {
        addSubject(subject);
        fieldsPoints.get(subject).put(grade, points);
    }

    public void addSubject(String subject) {
        if (!fieldsPoints.containsKey(subject)) {
            fieldsPoints.put(subject, new HashMap<>());
        }
    }

    private static final Comparator<Character> GRADE_COMPARATOR = new Comparator<Character>() {
        @Override
        public int compare(Character g1, Character g2) {
            return Integer.compare(GRADES.indexOf(g1), GRADES.indexOf(g2));
        }
    };

    @Override
    public String toString() {
        return "SubjectPoints{" +
                "tableIndex=" + tableIndex + ", " + "bestOf: " + bestOf +
                ", fieldsPoints=" + fieldsPoints +
                '}';
    }
}

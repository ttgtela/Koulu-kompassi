package com.jmnt.data;

import java.util.List;

public class UniversityTopField {
    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<UniversityPoints> getUniversityData() {
        return universityData;
    }

    public void setUniversityData(List<UniversityPoints> universityData) {
        this.universityData = universityData;
    }

    private List<UniversityPoints> universityData;
}

package com.jmnt.data;

import java.util.ArrayList;
import java.util.List;

public class WhereStudy {
    private String field;

    public List<String> getUniversities() {
        return universities;
    }

    public void setUniversities(List<String> universities) {
        this.universities = universities;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    private List<String> universities =  new ArrayList<String>();
}

package com.jmnt.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UniversityPoints {
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public UniversityPoints(String field, List<UniversityProgram> group, List<SubjectPoints> fieldsPoints, int tableIndex) {
        this.field = field;
        this.group = group;
        this.fieldsPoints = fieldsPoints;
        this.tableIndex = tableIndex;
    }

    @Override
    public String toString() {
        return "UniversityPoints{" +
                "field='" + field + '\'' +
                ", UniversityProgram='" + group + '\'' +
                ", SubjectPoints=" + fieldsPoints +
                ", tableIndex=" + tableIndex +
                '}';
    }

    private String field;

    public List<UniversityProgram> getGroup() {
        return group;
    }

    public void setGroup(List<UniversityProgram> group) {
        this.group = group;
    }

    public List<SubjectPoints> getFieldsPoints() {
        return fieldsPoints;
    }

    public void setFieldsPoints(List<SubjectPoints> fieldsPoints) {
        this.fieldsPoints = fieldsPoints;
    }

    private List<UniversityProgram> group;
    private List<SubjectPoints> fieldsPoints;

    public int getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    private int tableIndex;
}

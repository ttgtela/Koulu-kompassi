package com.jmnt.data;

import java.util.List;
import com.jmnt.data.University;

public class CombinedUniversityData {
    private String universityName;

    public String getTopField() {
        return topField;
    }

    public void setTopField(String topField) {
        this.topField = topField;
    }

    private String topField;

    public String getScrapedProgramName() {
        return scrapedProgramName;
    }

    public void setScrapedProgramName(String scrapedProgramName) {
        this.scrapedProgramName = scrapedProgramName;
    }

    private String scrapedProgramName;
    private String fieldName;
    private List<University.AdmissionMethod> admissionMethods; // From Excel data

    public List<SubjectPoints> getUniversityPoints() {
        return universityPoints;
    }

    public void setUniversityPoints(List<SubjectPoints> universityPoints) {
        this.universityPoints = universityPoints;
    }

    public List<University.AdmissionMethod> getAdmissionMethods() {
        return admissionMethods;
    }

    public void setAdmissionMethods(List<University.AdmissionMethod> admissionMethods) {
        this.admissionMethods = admissionMethods;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    private List<SubjectPoints> universityPoints; // From UniversityRequirements data
}

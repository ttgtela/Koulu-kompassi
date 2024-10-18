package com.jmnt.data;

import java.util.ArrayList;
import java.util.List;

public class University {
    private String name;

    public List<FieldOfStudy> getFields() {
        return fields;
    }

    public void setFields(List<FieldOfStudy> fields) {
        this.fields = fields;
    }

    private List<FieldOfStudy> fields;

    public University(String name) {
        this.name = name;
        this.fields = new ArrayList<>();
    }

    public void addField(FieldOfStudy field) {
        fields.add(field);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class FieldOfStudy {
        public void setName(String name) {
            this.name = name;
        }

        private String name;

        public List<AdmissionMethod> getAdmissionMethods() {
            return admissionMethods;
        }

        public void setAdmissionMethods(List<AdmissionMethod> admissionMethods) {
            this.admissionMethods = admissionMethods;
        }

        private List<AdmissionMethod> admissionMethods;

        public FieldOfStudy(String name) {
            this.name = name;
            this.admissionMethods = new ArrayList<>();
        }

        public void addAdmissionMethod(AdmissionMethod method) {
            admissionMethods.add(method);
        }

        public String getName() {
            return name;
        }

    }

    public static class AdmissionMethod {
        public void setName(String name) {
            this.name = name;
        }

        private String name;

        public void setRequiredPoints(double requiredPoints) {
            this.requiredPoints = requiredPoints;
        }

        private double requiredPoints;

        public AdmissionMethod(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public double getRequiredPoints() {
            return requiredPoints;
        }
    }
}




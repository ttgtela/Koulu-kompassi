package com.jmnt.data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ExcelParser {
    public static List<University> parse() {
        String filePath = "src/main/resources/ApplicationResources/pisterajat_2024.xlsx"; // path to your .xlsx file

        List<University> universities = new java.util.ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                for (Cell cell : row) {
                    switch (cell.getCellType()) {

                        case STRING:
                            if (cell.getCellStyle().getIndex() == 20){
                                University university=new University(cell.getStringCellValue());
                                universities.add(university);
                            }
                            else if (cell.getCellStyle().getDataFormatString().equals("General")){
                                if (cell.getCellStyle().getIndex() == 15){
                                    University.FieldOfStudy fieldOfStudy=new University.FieldOfStudy(cell.getStringCellValue());
                                    universities.getLast().addField(fieldOfStudy);
                                }
                                else if (!universities.isEmpty()){
                                    if (universities.getLast() != null &&
                                            universities.getLast().getFields() != null
                                            && universities.getLast().getFields().getLast() != null &&
                                            cell.getStringCellValue() != null) {
                                        universities.getLast().getFields().getLast().addAdmissionMethod(
                                                new University.AdmissionMethod(cell.getStringCellValue()));
                                    }
                                }

                            }
                            break;
                        case NUMERIC:
                            if (cell.getCellStyle().getDataFormatString().equals("#,##0.00")){
                                if (Double.valueOf(cell.getNumericCellValue()) !=null &&
                                !universities.getLast().getFields().getLast().getAdmissionMethods().isEmpty()){
                                    universities.getLast().getFields().getLast()
                                            .getAdmissionMethods()
                                            .getLast()
                                            .setRequiredPoints(cell.getNumericCellValue());

                                }
                                else if (!universities.getLast().getFields().getLast().getAdmissionMethods().isEmpty()){
                                    universities.getLast().getFields().getLast()
                                            .getAdmissionMethods()
                                            .getLast()
                                            .setRequiredPoints(0);

                                }

                            }
                            break;
                        case BOOLEAN:
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (University university : universities) {
            System.out.println("University name: "+university.getName());
            for (University.FieldOfStudy field : university.getFields()) {
                System.out.println("Field: "+field.getName());
                for (University.AdmissionMethod admissionMethod : field.getAdmissionMethods()) {
                    System.out.println("Admission Method: "+admissionMethod.getName());
                    System.out.println("Required points: "+admissionMethod.getRequiredPoints());
                }
            }
        }
        return universities;
    }
}
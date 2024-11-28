package com.jmnt.excelparser;

import com.jmnt.data.University;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelParser {
    public static Map<Integer,List<University>> parse() {
        List<String> filePaths = new ArrayList<String>();
        Map<Integer,List<University>> map = new HashMap<Integer,List<University>>();
        filePaths.add("src/main/resources/ApplicationResources/pisterajat_2020.xlsx");
        filePaths.add("src/main/resources/ApplicationResources/pisterajat_2021.xlsx");
        filePaths.add("src/main/resources/ApplicationResources/pisterajat_2022.xlsx");
        filePaths.add("src/main/resources/ApplicationResources/pisterajat_2023.xlsx");
        filePaths.add("src/main/resources/ApplicationResources/pisterajat_2024.xlsx");
        int start=2020;
        for (String filePath : filePaths) {
            List<University> universities=parseExcel(filePath);
            map.put(start,universities);
            start=start+1;
        }
        return map;
    }

    public static List<University> parseExcel(String filePath) {
            List<University> universities = new java.util.ArrayList<>();
            try (FileInputStream fis = new FileInputStream(filePath);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    for (Cell cell : row) {
                        switch (cell.getCellType()) {
                            case STRING:
                                if (cell.getCellStyle().getIndex() == 20) {
                                    University university = new University(cell.getStringCellValue());
                                    universities.add(university);
                                } else if (cell.getCellStyle().getDataFormatString().equals("General")) {
                                    if (cell.getCellStyle().getIndex() == 15) {
                                        University.FieldOfStudy fieldOfStudy = new University.FieldOfStudy(cell.getStringCellValue());
                                        universities.getLast().addField(fieldOfStudy);
                                    } else if (!universities.isEmpty()) {
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
                                if (cell.getCellStyle().getDataFormatString().equals("#,##0.00") && cell.getColumnIndex()==1) {
                                    if (Double.valueOf(cell.getNumericCellValue()) != null &&
                                            !universities.getLast().getFields().getLast().getAdmissionMethods().isEmpty()) {
                                        universities.getLast().getFields().getLast()
                                                .getAdmissionMethods()
                                                .getLast()
                                                .setRequiredPoints(cell.getNumericCellValue());

                                    } else if (!universities.getLast().getFields().getLast().getAdmissionMethods().isEmpty()) {
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
            return universities;
        }
    }

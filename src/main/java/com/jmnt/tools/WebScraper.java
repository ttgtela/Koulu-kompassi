package com.jmnt.tools;

import com.jmnt.data.SubjectPoints;
import com.jmnt.data.UniversityPoints;
import com.jmnt.utilities.GradeUtils;
import com.jmnt.data.UniversityProgram;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jmnt.utilities.GradeUtils.GRADES;

public class WebScraper {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
            + " AppleWebKit/537.36 (KHTML, like Gecko)"
            + " Chrome/58.0.3029.110 Safari/537.3";
    private static final int TIMEOUT = 5000;
    public static final List<String> NUMBERS = Arrays.asList(new String [] {
            "yksi","kaksi", "kolme", "neljä", "viisi", "kuusi", "seitsemän"});
    public static final List<String> FORBIDDEN_WORDS = Arrays.asList(new String [] {
            "Sisällysluettelo", "Hakukohde", "Yliopisto", "Kynnysehdot"});
    public static final List<String> SUBJECTS = Arrays.asList(new String [] {
            "äidinkieli", "biologia", "filosofia", "fysiikka", "historia", "kemia", "kieli, pitkä",
    "kieli, keskipitkä", "kieli, lyhyt", "maantiede", "psykologia", "terveystieto", "uskonto", "yhteiskuntaoppi"});
    private static final Pattern NUMBER_WORD_PATTERN = Pattern.compile("Pisteytystaulukko:\\s+(yksi|kaksi|kolme|neljä|viisi|kuusi|seitsemän|kahdeksan|yhdeksän)");

    private static final Map<String, Integer> FI_NUMBER_WORDS = new HashMap<>();
    private static final int h2Difference = -2;

    static {
        FI_NUMBER_WORDS.put("yksi", 1);
        FI_NUMBER_WORDS.put("kaksi", 2);
        FI_NUMBER_WORDS.put("kolme", 3);
        FI_NUMBER_WORDS.put("neljä", 4);
        FI_NUMBER_WORDS.put("viisi", 5);
        FI_NUMBER_WORDS.put("kuusi", 6);
        FI_NUMBER_WORDS.put("seitsemän", 7);
        FI_NUMBER_WORDS.put("kahdeksan", 8);
        FI_NUMBER_WORDS.put("yhdeksän", 9);
    }

    public static Integer convertToNumber(String word) {
        if (word == null) return null;
        return FI_NUMBER_WORDS.get(word.toLowerCase());
    }

    public static String extractNumberWord(String header) {
        if (header == null) return null;
        Matcher matcher = NUMBER_WORD_PATTERN.matcher(header);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static Document fetchDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT)
                .get();
    }

    public static List<String> getMainFieldNames(Document doc) {
        List<String> h2Texts = new ArrayList<>();

        Elements h2Elements = doc.select("h2");

        for (Element h2 : h2Elements) {
            String text = h2.text().trim();
            if (!text.isEmpty() && !FORBIDDEN_WORDS.contains(text)) {
                h2Texts.add(text);
            }
        }

        return h2Texts;
    }

    public static List<Integer> getBestPointsFromTableAmount(Document doc) {
        List<Integer> bestPointsFromTableAmount = new ArrayList<>();

        Elements h4Elements = doc.select("h4");

        for (Element h4 : h4Elements) {
            String text = h4.text().trim();
            for(String number : NUMBERS) {
                if(text.contains(number)) {
                    int index = NUMBERS.indexOf(number);
                    bestPointsFromTableAmount.add(index + 1);
                }
            }
        }
        return bestPointsFromTableAmount;
    }

    public static List<List<List<String>>> extractTables(Document doc) {
        List<List<List<String>>> tablesData = new ArrayList<>();

        Elements tables = doc.select("table");

        for (Element table : tables) {
            List<List<String>> tableData = new ArrayList<>();
            Elements rows = table.select("tr");

            for (Element row : rows) {
                List<String> rowData = new ArrayList<>();

                Elements headers = row.select("th");
                if (!headers.isEmpty()) {
                    for (Element header : headers) {
                        rowData.add(header.text().trim());
                    }
                } else {
                    Elements cols = row.select("td");
                    for (Element col : cols) {
                        rowData.add(col.text().trim());
                    }
                }

                if (!rowData.isEmpty()) {
                    tableData.add(rowData);
                }
            }

            if (!tableData.isEmpty()) {
                tablesData.add(tableData);
            }
        }

        return tablesData;
    }

    public static int getTableIndex(Document doc, Element target) {
        int count = 0;
        for (Element elem : doc.getAllElements()) {
            if (elem.tagName().equalsIgnoreCase("h2")) {
                count++;
            }
            if (elem.equals(target)) {
                break;
            }
        }
        return count;
    }

    public static List<UniversityProgram> getUniversityProgramData(Document doc) {
        List<UniversityProgram> programData = new ArrayList<>();

        Elements hakukohde = doc.select("[data-mtr-content=Hakukohde]");
        Elements yliopisto = doc.select("[data-mtr-content=Yliopisto]");
        Elements kynnysehdot = doc.select("[data-mtr-content=Kynnysehdot]");

        Elements h2Elements = doc.select("h2");

        for (int i = 0; i < hakukohde.size(); i++) {
            if(FORBIDDEN_WORDS.contains(hakukohde.get(i).text()) ||
                    FORBIDDEN_WORDS.contains(yliopisto.get(i).text()) ||
                    FORBIDDEN_WORDS.contains(kynnysehdot.get(i).text())) {continue;}
            String program = hakukohde.get(i).text().trim();
            String university = yliopisto.get(i).text().trim();
            Map<String, Character> required_grades = new HashMap<>();
            String[] parts = kynnysehdot.get(i).text().trim().split("\\s+ja\\s+");
            boolean same_grade = false;
            Character earlier_grade = '-';

            Element currentHakukohde = hakukohde.get(i);
            int tableIndex = getTableIndex(doc, currentHakukohde) + h2Difference;
            System.out.println("tableIndex " + tableIndex);

            if(parts.length >= 2) {
                same_grade = true;
            }

            for (int j = parts.length - 1; j >= 0; j--) {
                String subjectFound = null;
                Character gradeFound = '-';
                var part = parts[j];

                for (String subject : SUBJECTS) {
                    if (part.toLowerCase().contains(subject.toLowerCase())) {
                        subjectFound = subject;
                        if(same_grade) {
                            required_grades.put(subjectFound, earlier_grade);
                        }
                        break;
                    }
                }

                if (subjectFound != null) {
                    String gradeRegexString = "arvosanalla\\s+([LEMCBA])";
                    Pattern gradePattern = Pattern.compile(gradeRegexString, Pattern.CASE_INSENSITIVE);
                    Matcher gradeMatcher = gradePattern.matcher(part);

                    boolean found = gradeMatcher.find();
                    System.out.println("Part: " + part);
                    System.out.println("Grade found: " + found);
                    System.out.println("same_grade: " + same_grade);

                    if (found) {
                        String matchedGrade = gradeMatcher.group(1);
                        System.out.println("Added grade: " + matchedGrade);

                        gradeFound = matchedGrade.charAt(0);

                        if (same_grade) {
                            earlier_grade = gradeFound;
                        }
                    }
                    else {
                        if (same_grade) {
                            System.out.println("Earlier!");
                            System.out.println(earlier_grade);
                            required_grades.put(subjectFound, earlier_grade);
                            continue;
                        }
                    }

                    System.out.println("Added: " + gradeFound);
                    required_grades.put(subjectFound, gradeFound);
                }
            }

            UniversityProgram universityProgram = new UniversityProgram(program, university, required_grades, tableIndex);
            programData.add(universityProgram);
        }
        System.out.println(programData.size());
        return programData;
    }

    public static List<SubjectPoints> getSubjectPointsData(Document doc) {
        List<SubjectPoints> subjectPointsData = new ArrayList<>();

        Elements wrappers = doc.select("div.wpb_wrapper");
        System.out.println("Size: " + wrappers.size());
        int currentIndex = h2Difference;

        for (Element wrapper : wrappers) {
            Elements children = wrapper.children();
            boolean isPisteytysTaulukkoFound = false;
            var word = "-";
            int number = 0;

            for (Element child : children) {
                if (child.tagName().equalsIgnoreCase("h2")) {
                    currentIndex++;
                }
                if (child.tagName().equalsIgnoreCase("h4") && child.text().contains("Pisteytystaulukko")) {
                    isPisteytysTaulukkoFound = true;
                }
                if (isPisteytysTaulukkoFound) {
                    if(child.tagName().equalsIgnoreCase("h4") && child.text().contains("hakijalle")) {
                        word = extractNumberWord(child.text());
                        number = convertToNumber(word);
                        System.out.println(number);
                    }
                    if (child.tagName().equalsIgnoreCase("table")) {
                        List<SubjectPoints> parsedTables = parseTable(child, currentIndex, number);
                        subjectPointsData.addAll(parsedTables);
                    }
                }
            }
        }

        for (SubjectPoints sp : subjectPointsData) {
            System.out.println(sp);
        }

        return subjectPointsData;
    }

    private static List<SubjectPoints> parseTable(Element table, int index, int bestof) {
        List<SubjectPoints> subjectGrades = new ArrayList<>();

        Elements headerRows = table.select("thead tr");
        if (headerRows.isEmpty()) {
            System.err.println("Warning: No header row found in table.");
            return subjectGrades;
        }

        Element headerRow = headerRows.first();
        Elements headers = headerRow.select("th");

        List<Character> grades = new ArrayList<>();

        for (int i = 1; i < headers.size(); i++) {
            String gradeText = headers.get(i).text().trim().toUpperCase();
            if (gradeText.length() == 1 && GRADES.contains(gradeText.charAt(0))) {
                grades.add(gradeText.charAt(0));
            } else {
                System.err.println("Warning: Unexpected grade header: " + gradeText);
                grades.add('?');
            }
        }

        Elements dataRows = table.select("tbody tr");
        for (Element row : dataRows) {
            Elements cells = row.select("td");

            if (cells.size() < grades.size() + 1) {
                System.err.println("Warning: Row has insufficient number of cells: " + row.text());
                continue;
            }

            String subject = cells.get(0).text().trim();
            SubjectPoints sp = new SubjectPoints();

            for (int i = 1; i < cells.size(); i++) {
                String pointsStr = cells.get(i).text().trim();

                Character grade = grades.get(i - 1);

                pointsStr = pointsStr.replace(',', '.');

                Float points = 0.0f;
                try {
                    points = Float.parseFloat(pointsStr);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing points for subject '" + subject + "', grade '" + grade + "': " + pointsStr);
                    continue;
                }

                sp.updateGrade(subject, grade, points);
            }
            sp.setTableIndex(index);
            sp.setBestOf(bestof);
            subjectGrades.add(sp);
        }

        return subjectGrades;
    }

    /*
    public static List<String> getRequirements(List<List<List<String>>> doc) {
        List<List<List<String>>> tablesData = new ArrayList<>();

        Elements tables = doc.select("table");

        for (Element table : tables) {
            List<List<String>> tableData = new ArrayList<>();
            Elements rows = table.select("tr");

            for (Element row : rows) {
                List<String> rowData = new ArrayList<>();

                Elements headers = row.select("th");
                if (!headers.isEmpty()) {
                    for (Element header : headers) {
                        rowData.add(header.text().trim());
                    }
                } else {
                    Elements cols = row.select("td");
                    for (Element col : cols) {
                        rowData.add(col.text().trim());
                    }
                }

                if (!rowData.isEmpty()) {
                    tableData.add(rowData);
                }
            }

            if (!tableData.isEmpty()) {
                tablesData.add(tableData);
            }
        }

        return tablesData;
    }
     */
}

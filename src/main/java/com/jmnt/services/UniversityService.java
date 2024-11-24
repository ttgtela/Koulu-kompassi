package com.jmnt.services;

import com.jmnt.data.*;
import com.jmnt.excelparser.ExcelParser;
import com.jmnt.tools.WebScraper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UniversityService {

    private static final Logger logger = LoggerFactory.getLogger(UniversityService.class);

    public static final List<String> SCRAPED_WEBSITES = Arrays.asList(
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/biokemia-biologia-ja-ymparistotieteet-seka-biolaaketiede",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/elintarviketiede-ja-ravitsemustiede",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/farmasia-laaketieteelliset-alat-ja-terveystieteet",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/filosofia-historia-ja-teologia",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/fysikaaliset-tieteet-kemia-ja-matemaattiset-tieteet",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/geotieteet-ja-maantiede",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/informaatioverkostot-tietojenkasittelytiede-ja-tietojarjestelmatiede",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/kasvatusala",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/kauppatieteet-liiketaloustiede-ja-tietojenkasittelytiede-oikeustiede-seka-taloustiede",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/kirjallisuustieteet-kulttuurintutkimus-ja-taiteen-tutkimus-seka-saamelainen-kulttuuri",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/kotimaiset-kielet",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/liikunnan-yhteiskuntatieteet-liikuntabiologia-ja-liikuntapedagogiikka",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/logopedia-ja-psykologia",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/maataloustieteet-ja-metsatieteet",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/vieraat-kielet",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/viestintatieteet-ja-yhteiskuntatieteet"
    );

    public static final Set<String> FORBIDDEN_WORDS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "maisterihaku",
            "kandidatprogrammet",
            "magisterprogrammet",
            "master's",
            "master of science",
            "kandidat",
            "ammattikorkeakoulu",
            "amk",
            "bachelor",
            "magister"
    )));

    public static final String FILE_LOCATION = "src/main/java/com/jmnt/cache/";

    public static int CURRENT_YEAR = 2024;

    private static final long DELAY_MS = 2000;
    private static final boolean CACHE_ENABLED = false;

    private static int HEURISTIC_THRESHOLD = 15;

    public List<University> getUniversities(int year) {
        ParsedUniversityContext data = ParsedUniversityContext.getInstance();
        List<University> universities = data.getUniversities().get(year);
        if (universities == null) {
            logger.warn("No universities found for year: {}", year);
            return new ArrayList<>();
        }
        logger.info("Fetched {} universities for year {}", universities.size(), year);
        return universities;
    }

    public List<UniversityTopField> getUniversityRequirements() {
        List<UniversityTopField> universityTops = new ArrayList<>();

        PointsCache pointsCache = new PointsCache();
        Map<String, Collection<UniversityPoints>> cache = pointsCache.getCache();

        for (int i = 0; i < SCRAPED_WEBSITES.size(); i++) {
            UniversityTopField universityTop = new UniversityTopField();
            List<UniversityPoints> universityPoints = new ArrayList<>();
            boolean doc_exists = false;

            Document doc = null;
            try {
                String[] parts = SCRAPED_WEBSITES.get(i).split("/");
                String name = parts[parts.length - 1];
                File f = new File(FILE_LOCATION + name);
                if (f.exists()) {
                    String content = FileUtils.readFileToString(f, "UTF-8");
                    doc = Jsoup.parse(content);
                    doc_exists = true;
                    logger.debug("Loaded cached document for URL: {}", SCRAPED_WEBSITES.get(i));
                } else {
                    doc = WebScraper.fetchDocument(SCRAPED_WEBSITES.get(i));
                    FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
                    logger.debug("Fetched and cached document for URL: {}", SCRAPED_WEBSITES.get(i));
                }
            } catch (IOException e) {
                logger.error("Error fetching or writing document for URL: {}. Exception: {}", SCRAPED_WEBSITES.get(i), e.getMessage());
                continue;
            }

            List<String> mainHeaders = WebScraper.getMainFieldNames(doc);
            if (mainHeaders.size() < 2) {
                logger.warn("Not enough main headers found in the document for URL: {}", SCRAPED_WEBSITES.get(i));
                continue;
            }
            String headerKeyOriginal = mainHeaders.get(1).trim();
            String headerKey = normalizeFieldName(headerKeyOriginal);
            universityTop.setField(headerKeyOriginal);

            if (cache != null && CACHE_ENABLED && cache.containsKey(headerKey)) {
                Collection<UniversityPoints> cachedPoints = cache.get(headerKey);
                List<UniversityPoints> cachedList = new ArrayList<>(cachedPoints);

                universityTop.setUniversityData(cachedList);
                universityTops.add(universityTop);
                logger.info("Loaded cached data for field: {}", headerKeyOriginal);
                continue;
            }

            List<String> subHeaders = WebScraper.getSubFieldNames(doc);
            List<UniversityProgram> program_data = WebScraper.getUniversityProgramData(doc);
            List<SubjectPoints> subject_points = WebScraper.getSubjectPointsData(doc);

            for (UniversityProgram data : program_data) {
                logger.debug("Processing program data: {}", data.toString());
            }

            for (int j = 0; j < subHeaders.size(); j++) {
                List<UniversityProgram> program = new ArrayList<>();
                List<SubjectPoints> points = new ArrayList<>();
                for (UniversityProgram programData : program_data) {
                    if (programData.getTableIndex() == j) {
                        program.add(programData);
                    }
                }
                for (SubjectPoints subjectPoint : subject_points) {
                    if (subjectPoint.getTableIndex() == j) {
                        points.add(subjectPoint);
                    }
                }
                logger.info("programData, {}", subHeaders.get(j));
                String subHeader = subHeaders.get(j);
                if(subHeader.equals("Kiintiöt ja kynnysehdot")) {
                    subHeader = "Erityispedagogiikka ja erityisopettaja sekä opinto-ohjaaja ja uraohjaaja";
                }
                else if(subHeader.equals("Todistusvalinnan pisteytys: kasvatusala")) {
                    subHeader = "Kasvatusala";
                }
                UniversityPoints universityProgram = new UniversityPoints(subHeader, program, points, i);
                universityPoints.add(universityProgram);
            }

            universityTop.setUniversityData(universityPoints);
            universityTops.add(universityTop);
            logger.info("headerKey, {}", headerKey);
            cache.put(headerKey, universityPoints);
            logger.info("Processed and cached data for field: {}", headerKeyOriginal);

            try {
                if (!doc_exists) {
                    Thread.sleep(DELAY_MS);
                }
            } catch (InterruptedException e) {
                logger.error("Thread interrupted while sleeping: {}", e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
        pointsCache.save();
        logger.info("Fetched and processed University Requirements data successfully.");
        return universityTops;
    }

    static class RelevantData {
        RelevantData(List<SubjectPoints> subject_points, String field) {
            this.subject_points_ = subject_points;
            this.field_ = field;
        }

        public List<SubjectPoints> getSubject_points_() {
            return subject_points_;
        }

        public String getField_() {
            return field_;
        }

        List<SubjectPoints> subject_points_;
        String field_;
    }

    public List<CombinedUniversityData> getCombinedData() {
        List<University> excelUniversities = getUniversities(CURRENT_YEAR);
        List<CombinedUniversityData> combined = new ArrayList<>();

        List<University> filteredUniversities = excelUniversities.stream()
                .filter(univ -> {
                    String lowerName = univ.getName().toLowerCase();
                    return FORBIDDEN_WORDS.stream().noneMatch(lowerName::contains);
                })
                .collect(Collectors.toList());
        List<UniversityTopField> requirementsData = getUniversityRequirements();
        // fields from 0 to n
        for (University university : filteredUniversities) {
            String uniName = university.getName().toLowerCase();

            Map<String, RelevantData> scraped_points = new HashMap<>();

            for(UniversityTopField scraped : requirementsData) {
                for(var universityData : scraped.getUniversityData()) {
                    for(var group : universityData.getGroup()) {
                        if(group.getUniversity().toLowerCase().equals(uniName)) {
                            logger.info("Match: {} and {}", group.getUniversity(), uniName);
                            RelevantData data = new RelevantData(universityData.getFieldsPoints(), universityData.getField());
                            scraped_points.put(group.getProgram(), data);
                            break;
                        }
                    }
                }
            }
            var fields = university.getFields();

            for(var field : fields) {
                Locale finnishLocale = new Locale("fi", "FI");
                FuzzyScore fuzzyScore = new FuzzyScore(finnishLocale);

                for (Map.Entry<String, RelevantData> entry : scraped_points.entrySet()) {
                    int score = fuzzyScore.fuzzyScore(entry.getKey(), field.getName());

                    logger.info("Heuristic score for {}: {} is {}", university.getName(), field.getName(), score);

                    if(score >= HEURISTIC_THRESHOLD) {
                        logger.info("HEURISTIC good for: University {}, with the program {}", university.getName(), field.getName());
                        CombinedUniversityData combinedUniversityData = new CombinedUniversityData();
                        combinedUniversityData.setUniversityName(university.getName());
                        combinedUniversityData.setFieldName(field.getName());
                        combinedUniversityData.setUniversityPoints(entry.getValue().getSubject_points_());
                        combinedUniversityData.setTopField(entry.getValue().getField_());
                        combinedUniversityData.setScrapedProgramName(entry.getKey());
                        combinedUniversityData.setAdmissionMethods(field.getAdmissionMethods());

                        combined.add(combinedUniversityData);
                    }
                }
            }
        }

        return combined;
    }

    private String normalizeFieldName(String fieldName) {
        if (fieldName == null) return "";
        return fieldName.trim().toLowerCase()
                .replaceAll("[,\\.]", "")
                .replaceAll("\\s+", " ");
    }
}
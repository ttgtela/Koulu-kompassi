package com.jmnt.controllers;

import com.jmnt.data.*;
import com.jmnt.tools.WebScraper;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.formula.functions.T;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
public class UniversityRequirementsController {

    public static final List<String> SCRAPED_WEBSITES = Arrays.asList(new String [] {
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
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/maataloustieteet-ja-metsatieteet",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/vieraat-kielet",
            "https://yliopistovalinnat.fi/todistusvalinnan-pisteytykset-vuosina-2023-2025/viestintatieteet-ja-yhteiskuntatieteet",
    });

    public static final String FILE_LOCATION = "src/main/java/com/jmnt/cache/";

    private static final long DELAY_MS = 2000;
    private static final boolean CACHE_ENABLED = false;

    public List<UniversityTopField> getUniversityPoints() {
        List<UniversityTopField> universityTops = new ArrayList<>();

        PointsCache pointsCache = new PointsCache();
        Map<String, Collection<UniversityPoints>> cache = pointsCache.getCache();

        for(int i = 0; i < SCRAPED_WEBSITES.size(); i++) {
            UniversityTopField universityTop = new UniversityTopField();
            List<UniversityPoints> universityPoints = new ArrayList<>();
            boolean doc_exists = false;

            Document doc = null;
            try {
                String[] parts = SCRAPED_WEBSITES.get(i).split("/");
                String name = parts[parts.length - 1];
                File f = new File(FILE_LOCATION + name);
                if(f.exists()) {
                    String content = FileUtils.readFileToString(f, "UTF-8");
                    doc = Jsoup.parse(content);
                    doc_exists = true;
                }
                else {
                    doc = WebScraper.fetchDocument(SCRAPED_WEBSITES.get(i));
                    FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            List<String> mainHeaders = WebScraper.getMainFieldNames(doc);
            String headerKey = mainHeaders.get(1);
            universityTop.setField(headerKey); // get the second h1 header;

            if (cache != null && CACHE_ENABLED && cache.containsKey(headerKey)) {
                Collection<UniversityPoints> cachedPoints = cache.get(headerKey);
                List<UniversityPoints> cachedList = new ArrayList<>(cachedPoints);

                universityTop.setUniversityData(cachedList);
                universityTops.add(universityTop);
                continue;
            }

            List<String> subHeaders = WebScraper.getSubFieldNames(doc);
            List<UniversityProgram> program_data = WebScraper.getUniversityProgramData(doc);
            List<SubjectPoints> subject_points = WebScraper.getSubjectPointsData(doc);

            for(UniversityProgram data : program_data) {
                System.out.println(data.toString());
            }

            for(int j = 0; j < subHeaders.size(); j++) {
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
                UniversityPoints universityProgram = new UniversityPoints(subHeaders.get(j), program, points, i);
                universityPoints.add(universityProgram);
            }

            universityTop.setUniversityData(universityPoints);
            universityTops.add(universityTop);
            cache.put(universityTop.getField(), universityPoints);

            try {
                if(!doc_exists) {
                    Thread.sleep(DELAY_MS);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }
        }
        pointsCache.save();
        return universityTops;
    }

    @GetMapping("/api/points")
    public ResponseEntity<List<UniversityTopField>> GetPointsData() {
        try {
            var data = getUniversityPoints();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

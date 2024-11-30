package com.jmnt.controllers;

import com.jmnt.data.*;
import com.jmnt.services.UniversityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


/**
 * REST controller that provides endpoints to fetch university requirements and study options.
 * This controller integrates with the UniversityService to retrieve and process data.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")
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

    @Autowired
    private UniversityService universityService;

    public static final String FILE_LOCATION = "src/main/java/com/jmnt/cache/";

    private static final long DELAY_MS = 2000;
    private static final boolean CACHE_ENABLED = false;



    /**
     * Fetches university requirements data from the service layer.
     *
     * @return A ResponseEntity containing a list of UniversityTopField objects
     *         or an error response in case of failure.
     */
    @GetMapping("/api/points")
    public ResponseEntity<List<UniversityTopField>> getRequirementsData() {
        try {
            List<UniversityTopField> data = universityService.getUniversityRequirements();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            System.err.println("Error fetching requirements data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Fetches data about where students can study specific fields.
     *
     * @return A ResponseEntity containing a list of WhereStudy objects
     *         or an error response in case of failure.
     */
    @GetMapping("/api/wherestudy")
    public ResponseEntity<List<WhereStudy>> getWhereStudyData() {
        try {
            List<WhereStudy> data = universityService.getWhereStudy();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            System.err.println("Error fetching wherestudy data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /**
     * Fetches data about specific study programs for given fields.
     *
     * @return A ResponseEntity containing a list of WhereStudy objects
     *         or an error response in case of failure.
     */
    @GetMapping("/api/wherestudyspecific")
    public ResponseEntity<List<WhereStudy>> getWhereStudySpecificData() {
        try {
            List<WhereStudy> data = universityService.getWhereStudyProgrammes();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            System.err.println("Error fetching getWhereStudySpecificData data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

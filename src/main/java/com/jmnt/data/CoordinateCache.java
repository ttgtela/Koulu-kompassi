package com.jmnt.data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class CoordinateCache {
    // for every school name, there is map containing campus names and corresponding coordinates.
    private Map<String, Map<String, Coord>> cache;
    private final String CACHE_FILE_PATH = "src/main/java/com/jmnt/data/uniCoordinatesCache.json";

    public CoordinateCache() {
        this.cache = loadCacheFromFile();
    }

    private Map<String, Map<String, Coord>> loadCacheFromFile() {
        try (FileReader reader = new FileReader(CACHE_FILE_PATH)) {
            Gson gson = new Gson();
            Type cacheType = new TypeToken<Map<String, Map<String, Coord>>>() {}.getType();
            cache = gson.fromJson(reader, cacheType);
        } catch (IOException e) {
            System.out.println("Error reading " + CACHE_FILE_PATH);
            cache = new HashMap<>();
        }
        return cache;
    }

    public void saveCache(Map<String, Map<String, Coord>> coordMap) {
        try (FileWriter writer = new FileWriter(CACHE_FILE_PATH)) {
            Gson gson = new Gson();
            gson.toJson(coordMap, writer);
        } catch (IOException e) {
            System.out.println("Error saving cache to " + CACHE_FILE_PATH);
        }
    }

    public Map<String, Map<String, Coord>> getCache() {
        return cache;
    }
}

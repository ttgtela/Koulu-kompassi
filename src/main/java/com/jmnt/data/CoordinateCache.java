package com.jmnt.data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class CoordinateCache extends Cache<Map<String, CampusInfo>> {
    // for every school name, there is map containing campus names and corresponding coordinates.
    private Map<String, Map<String, CampusInfo>> cache;
    private final static String CACHE_FILE_PATH = "src/main/java/com/jmnt/cache/schoolCoordinatesCache.json";

    public CoordinateCache() {
        super(CACHE_FILE_PATH, getCacheType());
    }

    private static Type getCacheType() {
        return new TypeToken<Map<String, Map<String, CampusInfo>>>(){}.getType();
    }
}

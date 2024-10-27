package com.jmnt.data;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class PointsCache extends Cache<Map<String, UniversityPoints>> {
    private Map<String, Map<String, UniversityPoints>> cache;
    private final static String CACHE_FILE_PATH = "src/main/java/com/jmnt/data/uniPointsCache.json";

    public PointsCache() {
        super(CACHE_FILE_PATH, getCacheType());
    }

    private static Type getCacheType() {
        return new TypeToken<Map<String, Map<String, UniversityPoints>>>(){}.getType();
    }

    public void save() {
        saveCache();
    }
}

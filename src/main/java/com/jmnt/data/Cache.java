package com.jmnt.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.ss.formula.functions.T;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Cache<T> {
    private Map<String, T> cache;
    private final String filePath;
    private final Type typeT;

    public Cache(String filePath, Type typeT) {
        this.filePath = filePath;
        this.typeT = typeT;
        this.cache = loadCacheFromFile();
    }

    private Map<String, T> loadCacheFromFile() {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            cache = gson.fromJson(reader, typeT);
            if (cache == null) {
                cache = new HashMap<>();
            }
        } catch (IOException e) {
            System.out.println("Error reading " + filePath);
            cache = new HashMap<>();
        }
        return cache;
    }

    public void saveCache() {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new Gson();
            gson.toJson(this.cache, typeT, writer);
        } catch (IOException e) {
            System.out.println("Error saving cache to " + filePath);
        }
    }

    public Map<String, T> getCache() {
        return cache;
    }

    public void updateCache(String key, T value) {
        cache.put(key, value);
    }

    public T get(String key) {
        return cache.get(key);
    }
}

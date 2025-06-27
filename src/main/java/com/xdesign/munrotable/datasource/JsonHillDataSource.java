package com.xdesign.munrotable.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xdesign.munrotable.model.Hill;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonHillDataSource implements HillDataSource {

    private final File jsonFile;
    private final ObjectMapper objectMapper;

    public JsonHillDataSource(File jsonFile) {
        this.jsonFile = jsonFile;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<Hill> loadHills() {
        try {
            return List.of(objectMapper.readValue(jsonFile, Hill[].class));
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load hills from JSON file", ex);
        }
    }
}
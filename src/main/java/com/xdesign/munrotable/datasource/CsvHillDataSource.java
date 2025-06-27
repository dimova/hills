package com.xdesign.munrotable.datasource;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import com.xdesign.munrotable.model.Hill;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.xdesign.munrotable.model.Hill.Category.MUNRO;
import static com.xdesign.munrotable.model.Hill.Category.TOP;
import static com.xdesign.munrotable.util.CsvColumnHeading.*;

public class CsvHillDataSource implements HillDataSource {
    private final File csvFile;

    public CsvHillDataSource(File csvFile) {
        this.csvFile = csvFile;
    }

    @Override
    public List<Hill> loadHills() {
        try (var csvReader = new CSVReaderHeaderAware(new FileReader(csvFile))) {
            Map<String, String> rowData;
            var summits = new ArrayList<Hill>();
            while ((rowData = csvReader.readMap()) != null) {
                if (isQualifyingHill(rowData)) {
                    summits.add(createHill(rowData));
                }
            }
            return summits;
        } catch (IOException | CsvValidationException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static boolean isQualifyingHill(Map<String, String> rowData) {
        return isHillData(rowData) && !isDeletedHill(rowData);
    }

    private static boolean isHillData(Map<String, String> rowData) {
        var hillFields = List.of(NAME_FIELD, HEIGHT_FIELD, GRID_REFERENCE_FIELD, CATEGORY_FIELD);
        return rowData.entrySet().stream()
                .filter(e1 -> hillFields.contains(e1.getKey()))
                .noneMatch(e2 -> e2.getValue().isBlank());
    }

    private static boolean isDeletedHill(Map<String, String> rowData) {
        var category = rowData.get(CATEGORY_FIELD);
        return category.isBlank();
    }

    private static Hill createHill(Map<String, String> rowData) {
        var name = rowData.get(NAME_FIELD);
        var height = Double.parseDouble(rowData.get(HEIGHT_FIELD));
        var gridReference = rowData.get(GRID_REFERENCE_FIELD);
        var category = getCategory(rowData.get(CATEGORY_FIELD));
        return new Hill(name, height, gridReference, category);
    }

    private static Hill.Category getCategory(String value) {
        return switch (value.toUpperCase()) {
            case "MUN" -> MUNRO;
            case "TOP" -> TOP;
            default -> throw new IllegalArgumentException("Unknown hill category [%s]".formatted(value));
        };
    }
}
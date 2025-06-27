package com.xdesign.munrotable;

import com.xdesign.munrotable.datasource.CsvHillDataSource;
import com.xdesign.munrotable.datasource.HillDataSource;
import com.xdesign.munrotable.datasource.JsonHillDataSource;
import com.xdesign.munrotable.datasource.XmlHillDataSource;
import com.xdesign.munrotable.exception.CsvFileLoadingException;
import com.xdesign.munrotable.service.HillSearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class HillSearchApplication {

    @Value("${hill-search.munro.file.name}")
    private String munroDataFileName;

    @Value("${hill.json.file.name}")
    private String jsonFileName;

    @Value("${hill.xml.file.name}")
    private String xmlFileName;

    @Value("${hill.data.source.type:csv}")
    private String dataSourceType;

    @Bean
    public HillDataSource hillDataSource(File csvFile, File jsonFile, File xmlFile) {
        return switch (dataSourceType.toLowerCase()) {
            case "csv" -> new CsvHillDataSource(csvFile);
            case "json" -> new JsonHillDataSource(jsonFile);
            case "xml" -> new XmlHillDataSource(xmlFile);
            default -> throw new IllegalArgumentException("Unsupported data source type: " + dataSourceType);
        };
    }

    @Bean
    public HillSearchService hillSearchService(HillDataSource hillDataSource) {
        return new HillSearchService(hillDataSource);
    }

    @Bean
    public File csvFile() {
        try {
            return new ClassPathResource(munroDataFileName).getFile();
        } catch (IOException ex) {
            throw new CsvFileLoadingException(ex);
        }
    }

    @Bean
    public File jsonFile() {
        try {
            if (jsonFileName == null || jsonFileName.isBlank()) {
                throw new IllegalArgumentException("Munros JSON file name must be provided");
            }
            return new ClassPathResource(jsonFileName).getFile();
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to load Munros JSON file", e);
        }
    }

    @Bean
    public File xmlFile() {
        try {
            if (xmlFileName == null || xmlFileName.isBlank()) {
                throw new IllegalArgumentException("Munros XML file name must be provided");
            }
            return new ClassPathResource(xmlFileName).getFile();
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to load Munros XML file", e);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(HillSearchApplication.class, args);
    }
}
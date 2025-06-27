package com.xdesign.munrotable.datasource;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.xdesign.munrotable.model.Hill;
import com.xdesign.munrotable.model.HillsWrapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class XmlHillDataSource implements HillDataSource {

    private final File xmlFile;
    private final XmlMapper xmlMapper;

    public XmlHillDataSource(File xmlFile) {
        this.xmlFile = xmlFile;
        this.xmlMapper = new XmlMapper();
    }

    @Override
    public List<Hill> loadHills() {
        try {
            HillsWrapper wrapper = xmlMapper.readValue(xmlFile, HillsWrapper.class);
            return wrapper.getHills();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load hills from XML file", ex);
        }
    }
}

package com.xdesign.munrotable.datasource;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.xdesign.munrotable.model.Hill;
import com.xdesign.munrotable.model.Hills;


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
            Hills wrapper = xmlMapper.readValue(xmlFile, Hills.class);
            return wrapper.getHillList();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load hills from XML file", ex);
        }
    }
}

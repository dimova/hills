package com.xdesign.munrotable.model;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "hills")
public class Hills {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "hill")
    private List<Hill> hillList;

    public Hills() {}

    public Hills(List<Hill> hillList) {
        this.hillList = hillList;
    }

    public List<Hill> getHillList() {
        return hillList;
    }

    public void setHillList(List<Hill> hillList) {
        this.hillList = hillList;
    }
}

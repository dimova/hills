package com.xdesign.munrotable.datasource;

import com.xdesign.munrotable.model.Hill;
import java.util.List;

public interface HillDataSource {
    List<Hill> loadHills();
}
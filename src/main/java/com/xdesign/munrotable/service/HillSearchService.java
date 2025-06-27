package com.xdesign.munrotable.service;

import com.xdesign.munrotable.datasource.HillDataSource;
import com.xdesign.munrotable.dto.HillSearchRequest;
import com.xdesign.munrotable.dto.Sort;
import com.xdesign.munrotable.dto.SortField;
import com.xdesign.munrotable.dto.SortOrder;
import com.xdesign.munrotable.model.Hill;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;

public final class HillSearchService {

    private static final EnumMap<SortField, Comparator<Hill>> FIELD_COMPARATORS = new EnumMap<>(SortField.class);

    static {
        FIELD_COMPARATORS.put(SortField.HEIGHT, Comparator.comparing(
                Hill::height, Comparator.naturalOrder()));
        FIELD_COMPARATORS.put(SortField.NAME, Comparator.comparing(
                Hill::name, String.CASE_INSENSITIVE_ORDER));
    }

    private final HillDataSource dataSource;
    private List<Hill> cachedHills = null;

    public HillSearchService(HillDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Hill> searchHills(HillSearchRequest request) {
        if (cachedHills == null) {
            cachedHills = dataSource.loadHills();
        }
        return cachedHills.stream()
                .filter(hill -> request.category() == null || hill.category() == request.category())
                .filter(hill -> request.minHeight() == null || hill.height() >= request.minHeight())
                .filter(hill -> request.maxHeight() == null || hill.height() <= request.maxHeight())
                .sorted(buildComparator(request))
                .limit(request.limit())
                .toList();
    }

    private Comparator<Hill> buildComparator(HillSearchRequest request) {
        return request.sorts().stream()
                .map(this::getComparator)
                .reduce(Comparator::thenComparing)
                .orElse((h1, h2) -> 0);
    }

    private Comparator<Hill> getComparator(Sort sort) {
        var comparator = FIELD_COMPARATORS.get(sort.field());
        if (comparator == null) {
            throw new IllegalArgumentException("Unsupported sort field: " + sort.field());
        }
        return sort.order() == SortOrder.ASC ? comparator : comparator.reversed();
    }
}
package com.life.muna.product.domain;

import java.util.Arrays;

public enum LocationRange {
    SI("SI"),
    GU("GU"),
    DONG("DONG"),
    ALL("ALL");

    private final String locationRange;

    LocationRange(String locationRange) {
        this.locationRange = locationRange;
    }

    public String getLocationRange() {
        return locationRange;
    }

    public static LocationRange fromLocationRange(String locationRange) {
        return Arrays.stream(values())
                .filter(g -> g.getLocationRange().equals(locationRange))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown locationRange type: " + locationRange));
    }

}

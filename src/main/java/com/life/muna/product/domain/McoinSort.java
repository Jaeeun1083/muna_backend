package com.life.muna.product.domain;

import java.util.Arrays;

public enum McoinSort {
    DESC("DESC"),
    ASC("ASC"),
    NONE("NONE"),
    ;

    private final String priceSort;

    McoinSort(String priceSort) {
        this.priceSort = priceSort;
    }

    public String getMcoinSort() {
        return priceSort;
    }

    public static McoinSort fromMcoinSort(String priceSort) {
        return Arrays.stream(values())
                .filter(g -> g.getMcoinSort().equals(priceSort))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown priceSort type: " + priceSort));
    }

}

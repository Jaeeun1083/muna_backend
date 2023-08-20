package com.life.muna.product.domain.enums;

import java.util.Arrays;

public enum ProductStatus {
    AVL("AVAILABLE"),
    COMP("COMPLETED"),
    USD("USER_SUSPENDED"),
    UWD("USER_WITHDRAWAL"),
    DEL("DELETED");

    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static ProductStatus fromProductStatus(String status) {
        return Arrays.stream(values())
                .filter(g -> g.getStatus().equals(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown user status: " + status));
    }
}

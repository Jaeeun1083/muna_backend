package com.life.muna.user.domain.enums;

import java.util.Arrays;

public enum LoginType {
    KAKAO("KAKAO"),
    NAVER("NAVER"),
    EMAIL("EMAIL");

    private final String typeName;

    LoginType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static LoginType fromTypeName(String typeName) {
        return Arrays.stream(values())
                .filter(g -> g.getTypeName().equals(typeName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown login type: " + typeName));
    }
}

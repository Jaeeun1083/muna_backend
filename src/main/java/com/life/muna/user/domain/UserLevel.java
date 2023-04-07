package com.life.muna.user.domain;

import java.util.Arrays;

public enum UserLevel {
    BASIC("BASIC"),
    PREMIUM("PREMIUM"),
    VIP("VIP");

    private final String levelName;

    UserLevel(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelName() {
        return levelName;
    }

    public static UserLevel fromLevelName(String levelName) {
        return Arrays.stream(values())
                .filter(g -> g.getLevelName().equals(levelName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown user level: " + levelName));
    }
}

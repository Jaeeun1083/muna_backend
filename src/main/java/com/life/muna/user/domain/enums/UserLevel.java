package com.life.muna.user.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserLevel {
    //
    LV00("LV00", 0),
    LV01("LV01", 0),
    LV02("LV02", 2),
    LV03("LV03", 5),
    LV04("LV04", 10),
    LV05("LV05", 50);

    private final String levelName;
    private final Integer nanumCnt;

    UserLevel(String levelName, Integer nanumCnt) {
        this.levelName = levelName;
        this.nanumCnt = nanumCnt;
    }

    public static UserLevel fromLevelName(String levelName) {
        return Arrays.stream(values())
                .filter(g -> g.getLevelName().equals(levelName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown user level: " + levelName));
    }

    public int requiredNanumCnt() {
        UserLevel[] levels = values();
        int currentIndex = this.ordinal();
        if (currentIndex < levels.length - 1) {
            return levels[currentIndex + 1].getNanumCnt();
        } else {
            //가장 높은 레벨의 경우 0을 리턴함.
            return 0;
        }
    }

    public UserLevel getNextLevel(Integer nanumCnt) {
        int currentOrdinal = this.ordinal();
        if (currentOrdinal < UserLevel.values().length - 1) {
            UserLevel nextLevel = UserLevel.values()[currentOrdinal + 1];
            if (nextLevel.getNanumCnt() < (nanumCnt + 1)) {
                return nextLevel;
            }
        }
        return this;
    }

}

package com.life.muna.user.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserStatus {
    WD("WD"),
    SD("SD"),
    AC("AC");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

    public static UserStatus fromUserStatus(String status) {
        return Arrays.stream(values())
                .filter(g -> g.getStatus().equals(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown user status: " + status));
    }

}

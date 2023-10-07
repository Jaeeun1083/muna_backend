package com.life.muna.chat.domain.enums;

import lombok.Getter;

@Getter
public enum MessageType {
    CREATE("M000"),
    CHAT("M001"),
    CONF("M002"),
    NCONF("M003"),
    DONE("M004"),
    FAIL("M005"),
    LEAVE("M006"),
    BOT("M999"),
    ;
    private final String code;
    MessageType(String code) {
        this.code = code;
    }

}

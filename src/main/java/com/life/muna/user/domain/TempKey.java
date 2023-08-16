package com.life.muna.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TempKey {
    private String phone;
    private Integer tempKey;
    private int issuedCnt;
    private boolean verified;
    private LocalDateTime insertDate;
    private LocalDateTime updateDate;

    @Builder
    private TempKey(String phone, int tempKey, int issuedCnt, boolean verified, LocalDateTime insertDate, LocalDateTime updateDate) {
        this.phone = phone;
        this.tempKey = tempKey;
        this.issuedCnt = issuedCnt;
        this.verified = verified;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

    public void updateValue(int randomValue, int issuedCnt) {
        System.out.println(issuedCnt);
        this.tempKey = randomValue;
        this.issuedCnt = issuedCnt;
        this.verified = false;
    }

    public static TempKey createTempKey(int randomValue, String phone) {
        return TempKey.builder()
                .phone(phone)
                .tempKey(randomValue)
                .issuedCnt(1)
                .verified(false)
                .build();
    }

    public void verify() {
        this.verified = true;
    }

}

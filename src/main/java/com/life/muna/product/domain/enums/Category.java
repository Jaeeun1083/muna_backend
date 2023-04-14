package com.life.muna.product.domain.enums;

import lombok.Getter;

@Getter
public enum Category {
    C000("전체", "C000"), // ALL
    C001("전자기기", "C001"), // ELECTRONICS
    C002("생활가전", "C002"), // APPLIANCES
    C003("가구/인테리어", "C003"), // FURNITURE
    C004("생활/주방", "C004"), // KITCHEN
    C005("유아동", "C005"), // KIDS
    C006("뷰티/미용", "C006"), // BEAUTY
    C007("여성패션/잡화", "C007"), // WOMEN_FASHION
    C008("남성패션/잡화", "C008"), // MAN_FASHION
    C009("식품", "C009"), // FOOD
    C010("도서/음반/취미", "C010"), // MEDIA
    C011("반려동물용품", "C011"), // PET_SUPPLIES
    C012("티켓/교환권", "C012"), // TICKETS
    C013("기타", "C013") // ETC
    ;
    private final String data;
    private final String code;
    Category(String data, String code) {
        this.data = data;
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}

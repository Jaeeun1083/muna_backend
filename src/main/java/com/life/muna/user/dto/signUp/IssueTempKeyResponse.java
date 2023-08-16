package com.life.muna.user.dto.signUp;

import com.life.muna.user.domain.TempKey;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IssueTempKeyResponse {
    private Boolean result;
    private String phone;
    private int possibleCnt;
    private Long leftTime;

    @Builder
    private IssueTempKeyResponse(boolean result, String phone, int possibleCnt, long leftTime) {
        this.result = result;
        this.phone = phone;
        this.possibleCnt = possibleCnt;
        this.leftTime = leftTime;
    }

    public static IssueTempKeyResponse of(TempKey tempKey, int possibleCnt, Long leftTime) {
        return IssueTempKeyResponse.builder()
                .result(leftTime.equals(0L))
                .phone(tempKey.getPhone())
                .possibleCnt(possibleCnt)
                .leftTime(leftTime)
                .build();
    }

}

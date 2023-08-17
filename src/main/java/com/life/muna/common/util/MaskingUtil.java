package com.life.muna.common.util;

import org.springframework.stereotype.Component;

@Component
public class MaskingUtil {
    public static String getMaskedEmail(String email) {

        // @ 문자열의 인덱스값 추출
        int atIndex = email.indexOf("@");

        // 인덱스값이 0보다 크면
        if (atIndex > 1) {
            // 받아온 메일주소에 앞에 반절을 추출
            String maskedPart = email.substring(0, atIndex / 2); // 일부 마스킹 처리
            // 나머지 반절의 주소는 마스킹 처리
            for(int i = atIndex / 2;i < atIndex; i++){
                maskedPart += "*";
            }
            // @ 뒤에 부분 추출
            String domainPart = email.substring(atIndex);
            // 합쳐서 리턴
            return maskedPart + domainPart;
        } else if(atIndex == 1){
            // @ 뒤에 부분 추출
            String domainPart = email.substring(atIndex);
            // 합쳐서 리턴
            return "*" + domainPart;
        } else {
            return email;
        }
    }
}

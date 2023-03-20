package com.life.muna.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    /*400 BEAD REQUEST 잘못된 요청*/
    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),
    INVALID_PROVIDER(400, "올바른 인증 요청이 아닙니다."),
    INVALID_AUTH_TOKEN(400, "유효한 인증 정보가 아닙니다."),
    NOT_LOGIN_USER(400, "로그인 된 계정이 아닙니다."),

    /*403 FORBIDDEN 권한 인증 실패 */
    MISMATCH_MEMBER(403, "계정 정보가 일치하지 않습니다."),

    /*404 NOT FOUND 잘못된 리소스 접근*/
    NOT_FOUND_MEMBER(404, "계정을 찾을 수 없습니다."),

    /*409 충돌 */
    ALREADY_EXISTS_USER(409, "이미 존재하는 이메일입니다."),
    ALREADY_LOGIN_USER(409, "이미 로그인 된 계정입니다."),

    /*500 INTERNAL SERVER ERROR*/
    INTERNAL_SERVER_ERROR(500, "서버에서 요청을 처리하지 못했습니다. 다시 시도 해주세요."),
    ;

    private final int statusCode;
    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

}

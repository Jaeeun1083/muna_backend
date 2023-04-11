package com.life.muna.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    /* USER ErrorCode */
    NOT_LOGIN_USER(400, "로그인 된 계정이 아닙니다."),
    DISABLED_USER(403, "비활성화된 계정입니다."),
    MISMATCH_USER(400, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER(404, "계정을 찾을 수 없습니다."),
    ALREADY_EXISTS_USER(409, "이미 존재하는 이메일입니다."),
    ALREADY_LOGIN_USER(409, "이미 로그인 된 계정입니다."),

    /* AUTH ErrorCode */
    INVALID_AUTH_TOKEN(401, "유효한 인증 정보가 아닙니다."),
    NOT_FOUND_BY_USER_CODE(404, "해당 유저 코드로 계정을 찾을 수 없습니다."),
    NECESSARY_USER_CODE(400, "헤더에 user code 값은 필수입니다."),
    MISMATCH_TOKEN_USER_CODE(400, "토큰 값과 유저 코드 값이 일치하지 않습니다."),

    /* PRODUCT ErrorCode */
    UNPROCESSABLE_PRODUCT_LOCATION(422, "범위 조회 시 위치 코드가 필요합니다."),
    NOT_FOUND_PRODUCT_DETAIL(400, "해당 코드의 상품이 없습니다."),
    NOT_FOUND_SELLER(400, "해당 상품을 등록한 유저를 조회할 수 없습니다."),

    /* Common ErrorCode */
    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),

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

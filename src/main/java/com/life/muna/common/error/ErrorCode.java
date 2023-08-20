package com.life.muna.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    /**
     * USER ERROR CODE
     * */
    NOT_LOGIN_USER(1000, "로그인 된 계정이 아닙니다."),
    ALREADY_LOGIN_USER(1001, "이미 로그인 된 계정입니다."),
    DISABLED_USER(1002, "비활성화된 계정입니다."),
    MISMATCH_PASSWORD(1003, "비밀번호가 일치하지 않습니다."),
    MISMATCH_LOGIN_TYPE(1004, "로그인 타입이 맞지 않습니다."),
    NOT_FOUND_USER(1005, "계정을 찾을 수 없습니다."),
    SUSPENDED_USER(1006, "정지된 회원입니다."),

    ALREADY_EXISTS_EMAIL(1007, "이미 존재하는 이메일입니다."),
    ALREADY_EXISTS_NICKNAME(1008, "이미 존재하는 닉네임입니다."),
    ALREADY_EXISTS_PHONE(1008, "이미 존재하는 휴대폰 번호 입니다."),

    INVALID_EMAIL(1009, "이메일 형식이 올바르지 않습니다."),
    INVALID_NICKNAME(1010, "닉네임은 영문 또는 한글 또는 숫자이어야 합니다."),
    INVALID_PASSWORD(1011, "비밀번호는 영문과 숫자또는 특수문자 조합으로 8자 ~ 20자의 비밀번호여야 합니다."),
    INVALID_PHONE(1012, "10~11자리의 숫자만 입력가능합니다."),
    CANNOT_RESET_PASSWORD_USER(1013, "이메일로 가입한 경우에만 비밀번호 찾기가 가능합니다."),

    /**
     * TEMPKEY ERROR CODE
     * */
    EXCEED_ISSUED_TEMPKEY_COUNT(1013, "신청 가능한 횟수를 넘었습니다."),
    NOT_FOUND_TEMPKEY(1014, "인증 키 발급을 해주세요."),
    NOT_VERIFY_TEMPKEY(1015, "본인 인증을 먼저 해주세요."),

    /**
     * AUTH ERROR CODE
     * */
    INVALID_ACCESS_TOKEN(2000, "유효한 인증 정보가 아닙니다."),
    INVALID_REFRESH_TOKEN(2001, "유효한 인증 정보가 아닙니다."),
    MISMATCH_TOKEN_USER_CODE(2002, "토큰 값과 유저 코드 값이 일치하지 않습니다."),

    /**
     * PRODUCT ERROR CODE
     * */
    NOT_FOUND_PRODUCT(3000, "해당 코드의 상품을 찾을 수 없습니다."),
    CANNOT_MODIFY_PRODUCT(3001, "해당 상품은 수정할 수 없습니다."),
    CANNOT_DELETE_DONE_PRODUCT(3002, "거래가 완료된 상품은 삭제할 수 없습니다."),
    CANNOT_DELETE_CONFIRMED_PRODUCT(3003, "나눔 확정 중인 상품은 삭제할 수 없습니다."),
    NOT_FOUND_SELLER(3003, "해당 상품을 등록한 회원을 조회할 수 없습니다."),
    NOT_FOUND_RECEIVER(3004, "상대방을 조회할 수 없습니다."),
    CANNOT_SEARCH_OTHER_PRODUCT(3004, "다른 사람의 상품에 관해서는 조회할 수 없습니다."),
    NO_AUTHENTICATION_PRODUCT(3005, "해당 요청에 대한 권한이 없습니다."),
    FAILED_TO_CREATE_PRODUCT(3006, "나눔 상품을 등록 하는데 실패 하였습니다. 잠시 후 다시 시도해주세요."),
    NO_IMAGES_TO_UPLOAD(3007, "나눔 상품을 등록하기 위해서는 적어도 하나의 이미지가 필요합니다."),
    FAILED_TO_DELETE_DIRECTORY(3008, "이미지 저장에 실패하였습니다. 잠시 후 다시 시도해주세요."),
    FAILED_TO_CREATE_DIRECTORY(3009, "이미지 저장에 실패하였습니다. 잠시 후 다시 시도해주세요."),
    FAILED_TO_SAVE_IMAGE(3010, "이미지 저장에 실패하였습니다. 잠시 후 다시 시도해주세요."),

    /**
     * REQ PRODUCT ERROR CODE
     * */
    NOT_FOUND_PRODUCT_REQ(3101, "나눔 요청이 취소된 상태입니다."),
    DISABLED_PRODUCT_REQUEST(3102, "나눔 요청이 불가능한 상품입니다."),
    CANNOT_REQUEST_MY_PRODUCT(3103, "자신이 등록한 상품은 나눔 요청을 할 수 없습니다."),
    ALREADY_PRODUCT_REQUEST(3104, "이미 나눔을 요청한 상품입니다."),
    EXCEED_PRODUCT_REQUEST_COUNT(3105, "신청 가능한 나눔 요청 횟수를 넘었습니다."),

    /**
     * LIKE PRODUCT ERROR CODE
     * */
    DISABLED_PRODUCT_LIKE(3201, "좋아요 요청이 불가능한 상품입니다."),
    CANNOT_REQUEST_MY_LIKE(3202, "자신이 등록한 상품은 좋아요 요청을 할 수 없습니다."),
    ALREADY_PRODUCT_LIKE(3203, "이미 좋아요을 요청한 상품입니다."),
    NOT_FOUND_PRODUCT_LIKE(3204, "해당 상품에 보낸 요청을 찾을 수 없습니다."),

    /**
     * CHAT ERROR CODE
     * */
    ONLY_CREATE_MY_PRODUCTS(4000, "자신이 등록한 상품에만 채팅 생성이 가능합니다."),
    DISABLED_CREATE_CHAT(4001, "채팅을 생성할 수 없는 요청입니다."),
    ALREADY_EXISTS_CHAT(4002, "이미 채팅이 생성되어 있습니다."),
    NOT_FOUND_CHATROOM(4003, "채팅방을 찾을 수 없습니다."),
    INVALID_USER_OF_CHAT(4004, "해당 채팅방에 대한 권한이 없는 유저입니다."),
    CANNOT_CONFIRM_CHAT_SELLER(4005, "아직 참여자가 채팅에 들어오지 않아 나눔 확정을 누를 수 없습니다."),
    NOT_ACTIVE_STATUS(4006, "채팅에 들어오지 않아 채팅을 보낼 수 없습니다."),
    DISABLED_CONFIRM_COMPLETED_CHAT(4007, "완료된 상품은 나눔 확정을 누를 수 없습니다."),
    REQUESTER_WITHDRAW_REQUEST(4008, "요청자가 나눔 요청을 취소하여 채팅이 불가능합니다."),
    CANNOT_CONFIRM_CHAT_REQUESTER(4009, "나눔 확정을 누를 수 없습니다."),
    CANNOT_CONFIRM_DELETED_PRODUCT(4010, "상품이 삭제 되어 거래를 할 수 없습니다."),
    CANNOT_CONFIRM_CHAT_MANY_REQUESTER(4011, "여러 개의 상품에 나눔 확정을 누를 수 없습니다."),

    /**
     * LOCATION ERROR CODE
     * */
    INVALID_LOCATION_RANGE_CODE(5000, "위치 범위 코드가 올바르지 않습니다"),
    INVALID_LOCATION_PARAMETER(5001, "위치 조회는 3 ~ 5 단어까지만 가능합니다."),
    UNPROCESSABLE_LOCATION(5002, "범위 조회 시 위치 코드가 필요합니다."),
    UNPROCESSABLE_USER_LOCATION(5003, "사용자의 위치 설정을 먼저 해주세요."),
    NOT_FOUND_LOCATION(5004, "등록되지 않은 위치입니다."),


    /**
     * NOTIFICATION ERROR CODE
     * */
    INVALID_SUBSCRIBE_DESTINATION(6000, "잘못된 구독 정보 입니다."),
    FAIL_TO_SEND_FCM(6000, "알림 보내기에 실패하였습니다."),
    CAN_NOT_FOUND_TOKEN(6001, "해당 유저의 FCM 토큰이 존재하지 않습니다."),

    /**
     * BLOCKED ERROR CODE
     * */
    BLOCKED_CHATROOM(7001, ""),
    BLOCKED_REQUEST(7002, ""),

    /**
     * EVENT ERROR CODE
     * */
    EXCEEDED_EVENT_REQUEST(7500, "가능한 이벤트 요청 횟수를 초과했습니다."),
    DISABLED_REQUEST_EVENT_DURATION(7501, "이벤트 요청이 가능한 기간이 아닙니다."),

    /**
     * COMMON ERROR CODE
     * */
    INVALID_PARAMETER(8000, "파라미터 값을 확인해주세요."),

    /**
     * INTERNAL SERVER ERROR CODE
     * */
    INTERNAL_SERVER_ERROR(9999, "서버에서 요청을 처리하지 못했습니다. 다시 시도 해주세요."),
    METHOD_VALID_EXCEPTION(9998, "유효성 검사에 실패하였습니다. 값을 다시 확인해주세요.");

    private final int errorCode;
    private final String message;

    ErrorCode(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

}

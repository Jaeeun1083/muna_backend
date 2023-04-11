package com.life.muna.chat.domain;

import lombok.Getter;

import java.util.Date;

@Getter
public class ChatReq {
    private Long chatReqCode;
    private Long productCode;
    private Long userCode;
    private String reqContent;
    private Date insertDate;
    private Date updateDate;
}

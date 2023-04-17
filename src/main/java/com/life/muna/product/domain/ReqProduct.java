package com.life.muna.product.domain;

import com.life.muna.product.domain.enums.ChatStatus;
import lombok.Getter;

import java.util.Date;

@Getter
public class ReqProduct {
    private Long productCode;
    private Long userCode;
    private String reqContent;
    private ChatStatus chatStatus;
    private Date insertDate;
    private Date updateDate;
}

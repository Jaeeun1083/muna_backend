package com.life.muna.productReq.domain;

import com.life.muna.productReq.domain.enums.ReqStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReqProduct {
    private Long productReqCode;
    private Long productCode;
    private Long sellerCode;
    private Long requesterCode;
    private boolean cashedReq;
    private String reqContent;
    private Boolean reqRead;
    private ReqStatus reqStatus;
    private LocalDateTime insertDate;
    private LocalDateTime updateDate;

    @Builder
    private ReqProduct(Long productReqCode, Long productCode, Long sellerCode, Long requesterCode, boolean cashedReq, String reqContent, Boolean reqRead, ReqStatus reqStatus, LocalDateTime insertDate, LocalDateTime updateDate) {
        this.productReqCode = productReqCode;
        this.productCode = productCode;
        this.sellerCode = sellerCode;
        this.requesterCode = requesterCode;
        this.cashedReq = cashedReq;
        this.reqContent = reqContent;
        this.reqRead = reqRead;
        this.reqStatus = reqStatus;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

}

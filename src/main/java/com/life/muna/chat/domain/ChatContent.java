package com.life.muna.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class ChatContent {
    private Long productReqCode;
    private Long senderCode;
    private Long recipientCode;
    private String message;
    private boolean messageRead;
    private Date sendDate;

    @Builder
    private ChatContent(Long productReqCode, Long senderCode, Long recipientCode, String message, boolean messageRead, Date sendDate) {
        this.productReqCode = productReqCode;
        this.senderCode = senderCode;
        this.recipientCode = recipientCode;
        this.message = message;
        this.messageRead = messageRead;
        this.sendDate = sendDate;
    }

    @Override
    public String toString() {
        return "ChatContent{" +
                "productReqCode=" + productReqCode +
                ", senderCode=" + senderCode +
                ", recipientCode=" + recipientCode +
                ", message='" + message + '\'' +
                ", messageRead=" + messageRead +
                ", sendDate=" + sendDate +
                '}';
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

package com.life.muna.chat.domain;

import com.life.muna.chat.domain.enums.ChatStatus;
import com.life.muna.productReq.domain.ReqProduct;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
public class ChatRoom {
    private Long productReqCode;
    private Long productCode;
    private Long sellerCode;
    private Long requesterCode;
    private String roomCode;
    private boolean sellerActive;
    private boolean requesterActive;
    private ChatStatus chatStatus;
    private Date insertDate;
    private Date updateDate;

    @Builder
    private ChatRoom(Long productReqCode, Long productCode, Long sellerCode, Long requesterCode, String roomCode, boolean sellerActive, boolean requesterActive, ChatStatus chatStatus, Date insertDate, Date updateDate) {
        this.productReqCode = productReqCode;
        this.productCode = productCode;
        this.sellerCode = sellerCode;
        this.requesterCode = requesterCode;
        this.roomCode = roomCode;
        this.sellerActive = sellerActive;
        this.requesterActive = requesterActive;
        this.chatStatus = chatStatus;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

    public static ChatRoom createRoom(ReqProduct reqProduct) {
        return ChatRoom.builder()
                .productReqCode(reqProduct.getProductReqCode())
                .productCode(reqProduct.getProductCode())
                .sellerCode(reqProduct.getSellerCode())
                .requesterCode(reqProduct.getRequesterCode())
                .roomCode(UUID.randomUUID().toString())
                .sellerActive(false)
                .requesterActive(false)
                .chatStatus(ChatStatus.NCONF)
                .build();
    }

}

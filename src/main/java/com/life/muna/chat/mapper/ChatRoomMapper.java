package com.life.muna.chat.mapper;

import com.life.muna.chat.domain.ChatContent;
import com.life.muna.chat.domain.ChatRoom;
import com.life.muna.chat.domain.enums.ChatStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChatRoomMapper {
    List<ChatRoom> findAllByUserCode(@Param("userCode") Long userCode);
    List<ChatRoom> findAllByProductCode(@Param("productCode") Long productCode);
    Optional<ChatRoom> findByRequesterCodeAndProductCode(@Param("requesterCode") Long requesterCode, @Param("productCode") Long productCode);
    Optional<ChatRoom> findByProductReqCode(@Param("productReqCode") Long productReqCode);
    Optional<ChatRoom> findByRoomCode(@Param("roomCode") String roomCode);
    int updateSellerActive(@Param("productReqCode") Long productReqCode,@Param("status") boolean status);
    int updateRequesterActive(@Param("productReqCode") Long productReqCode,@Param("status") boolean status);
    int updateActive(@Param("productReqCode") Long productReqCode, @Param("requesterActive") boolean requesterActive, @Param("sellerActive") boolean sellerActive);
    int saveChatRoom(ChatRoom chatRoom);
    int saveChatStatus(@Param("productReqCode") Long productReqCode, @Param("isSeller") boolean isSeller, @Param("status") boolean status, @Param("chatStatus") ChatStatus chatStatus);
    int saveMessageRead(@Param("productReqCode") Long productReqCode, @Param("recipientCode") Long recipientCode, @Param("messageRead") boolean messageRead);
    List<ChatContent> findAllByChatReqCode(@Param("productReqCode") Long productReqCode);

    Optional<ChatContent> findLastChatByChatReqCode(@Param("productReqCode") Long productReqCode);
    int saveChatContent(ChatContent chatContent);
    ChatStatus findProductReqCodeToChatStatus(Long productReqCode);

}

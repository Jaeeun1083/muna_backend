package com.life.muna.chat.util;

import com.life.muna.auth.util.JwtTokenProvider;
import com.life.muna.chat.domain.ChatRoom;
import com.life.muna.chat.mapper.ChatRoomMapper;
import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.user.domain.User;
import com.life.muna.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.life.muna.auth.util.AuthorizationExtractor.BEARER_TYPE;

@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private Map<String, String> emailSessionMap = new ConcurrentHashMap<>();
    private Map<String, String> destinationRoomCodeSessionMap = new ConcurrentHashMap<>();

    private final String SUB_LINE_PREFIX = "/sub/chat/room/";

    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;
    private final ChatRoomMapper chatRoomMapper;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            String emailFromToken = getEmailByAuthorizationHeader(headerAccessor);
            String sessionId = headerAccessor.getSessionId();
            emailSessionMap.put(sessionId, emailFromToken);
        } else if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            String destination = headerAccessor.getDestination();
            String roomCode = extractRoomCodeFromDestination(destination);
            String sessionId = headerAccessor.getSessionId();
            String storedEmail = emailSessionMap.get(sessionId);
            if (!roomCode.isEmpty()) {
                destinationRoomCodeSessionMap.put(sessionId, roomCode);
            }

//            // subscribe를 호출한 유저.
            User user = userMapper.findByEmail(storedEmail).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
            Optional<ChatRoom> chatRoomOptional = chatRoomMapper.findByRoomCode(roomCode);

            if (chatRoomOptional.isPresent()) {
                ChatRoom findChatRoom = chatRoomOptional.get();
                Long userCode = user.getUserCode();
                chatRoomMapper.saveMessageRead(findChatRoom.getProductReqCode(), userCode, true);
            }
            // 구독 대상에 따른 추가 로직 수행
        } else if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())) {
            String sessionId = headerAccessor.getSessionId();
            emailSessionMap.remove(sessionId);
            destinationRoomCodeSessionMap.remove(sessionId);
        } else if (StompCommand.SEND.equals(headerAccessor.getCommand())) {
            String sessionId = headerAccessor.getSessionId();

            MessageBuilder<?> messageBuilder = MessageBuilder.fromMessage(message);
            boolean receiverConnected = false;
//            if (!roomCode.isEmpty()) {
                String email = emailSessionMap.get(sessionId);
                String storedRoomCode = destinationRoomCodeSessionMap.get(sessionId);

//                if (storedRoomCode != null && storedRoomCode.equals(roomCode)) {
                    // 같은 roomCode에 다른 sessionId가 존재하는지 확인₩₩
                    for (String storedSessionId : destinationRoomCodeSessionMap.keySet()) {
                        if (!storedSessionId.equals(sessionId) && storedRoomCode.equals(destinationRoomCodeSessionMap.get(storedSessionId))) {
                            // 동일한 roomCode에 다른 sessionId가 존재함
                            receiverConnected = true;
                            break;
                        }
                    }
//                }
//            }
            messageBuilder.setHeader("receiverConnected", receiverConnected);
            message = messageBuilder.build();
        }
        return message;
    }
    private String extractRoomCodeFromDestination(String destination) {
        if (destination == null || !destination.startsWith(SUB_LINE_PREFIX)) throw new BusinessException(ErrorCode.INVALID_SUBSCRIBE_DESTINATION);
        return destination.replace(SUB_LINE_PREFIX, "");
    }

    private String getEmailByAuthorizationHeader(StompHeaderAccessor headerAccessor) {
        List<String> authorization = headerAccessor.getNativeHeader("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        String value = authorization.get(0);
        if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
            String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
            return  tokenProvider.validAndGetEmailFromToken(authHeaderValue);
        }
        return  tokenProvider.validAndGetEmailFromToken(value);

    }

}

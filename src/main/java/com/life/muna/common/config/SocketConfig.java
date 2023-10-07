package com.life.muna.common.config;

import com.life.muna.chat.util.StompErrorHandler;
import com.life.muna.chat.util.StompInterceptor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class SocketConfig implements WebSocketMessageBrokerConfigurer {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final StompInterceptor stompInterceptor;

    @Bean
    public StompErrorHandler stompErrorHandler() {
        return new StompErrorHandler();
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트에서 WebSocket에 접속할 수 있는 endpoint를 지정한다.
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*");
        registry.setErrorHandler(stompErrorHandler());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) { // configureMessageBroker에서는 메시지를 중간에서 라우팅할 때 사용하는 메시지 브로커를 구성한다.
        // enableSimpleBroker에서는 해당 주소를 구독하는 클라이언트에게 메시지를 보낸다. 즉, 인자에는 구독 요청의 prefix를 넣고, 클라이언트에서 1번 채널을 구독하고자 할 때는 /sub/1 형식과 같은 규칙을 따라야 한다.
        registry.enableSimpleBroker("/sub");
        // setApplicationDestinationPrefixes에는 메시지 발행 요청의 prefix를 넣는다. 즉, /pub로 시작하는 메시지만 해당 Broker에서 받아서 처리한다.
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // 사용자가 웹 소켓 연결에 연결 될 때와 끊길 때 추가 기능(인증, 세션 관리 등)을 위해 인터셉터를 걸어주었다. 인자에는 추가 기능을 구현한 StompHandler를 빈으로 등록하여 넣어주었다.
        registration.interceptors(stompInterceptor);
    }
}

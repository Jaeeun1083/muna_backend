package com.life.muna.auth.util;

import com.life.muna.user.domain.User;
import com.life.muna.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final String EXPIRATION_KEY = "__keyevent@*__:expired";
    private final UserMapper userMapper;

    /**
     * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
     *
     * @param listenerContainer must not be {@literal null}.
     * @param userMapper
     */
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer, UserMapper userMapper) {
        super(listenerContainer);
        this.userMapper = userMapper;
    }

    /**
     *
     * @param message   redis key
     * @param pattern   __keyevent@*__:expired
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        LOG.info("RedisKeyExpirationListener onMessage pattern " + new String(pattern) + " | " + message.toString());
        if (new String(pattern).equals(EXPIRATION_KEY)) {
            Optional<User> userOptional = userMapper.findByEmail(message.toString());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                userMapper.saveFcmToken(user.getUserCode(), null);
            }
        }
    }
}

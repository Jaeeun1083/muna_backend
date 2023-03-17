package com.life.muna.user.mapper;

import com.life.muna.user.domain.User;
import com.life.muna.user.dto.SignInRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    boolean existsByEmail(String userEmail);
    User getUserByEmail(SignInRequest signInRequest);
    int signUp(User user);
}

package com.life.muna.user.mapper;

import com.life.muna.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    boolean existsByEmail(String userEmail);
    User findUserByEmail(String userEmail);
    User findUserByUserCode(Long userCode);
    int signUp(User user);
}

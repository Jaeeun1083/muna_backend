package com.life.muna.user.mapper;

import com.life.muna.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface UserMapper {
    boolean existsByEmail(String userEmail);
    Optional<User>  findUserByEmail(String userEmail);
    Optional<User> findUserByUserCode(Long userCode);
    String findEmailByUserCode(Long userCode);
    int signUp(User user);
}

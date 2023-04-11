package com.life.muna.user.mapper;

import com.life.muna.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface UserMapper {
    boolean existsByEmail(@Param("userEmail") String userEmail);
    Optional<User>  findUserByEmail(@Param("userEmail") String userEmail);
    Optional<User> findUserByUserCode(@Param("userCode") Long userCode);
    String findEmailByUserCode(@Param("userCode") Long userCode);
    int signUp(User user);
}

package com.life.muna.user.mapper;

import com.life.muna.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface UserMapper {
    boolean existsByNickName(@Param("nickname") String nickname);
    boolean existsByEmail(@Param("userEmail") String userEmail);
    Optional<User>  findUserByEmail(@Param("userEmail") String userEmail);
    Long findUserCodeByEmail(@Param("email") String email);
    Optional<User> findUserByUserCode(@Param("userCode") Long userCode);
    String findEmailByUserCode(@Param("userCode") Long userCode);
    int save(User user);
}

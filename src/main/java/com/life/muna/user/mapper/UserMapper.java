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
    boolean existsByEmail(@Param("email") String email);
    boolean existsByPhone(@Param("phone") String phone);
    Optional<User> findByEmail(@Param("email") String userEmail);
    Optional<User> findByUserCode(@Param("userCode") Long userCode);
    int saveLocation(@Param("userCode") Long userCode, @Param("locationDongCd") Long locationDongCd);
    int saveReqCnt(@Param("userCode") Long userCode, @Param("reqCnt") int reqCnt, @Param("cashedReqCnt") int cashedReqCnt);
    int savePassword(@Param("userCode") Long userCode, @Param("password") String password);
    int saveFcmToken(@Param("userCode") Long userCode,  @Param("fcmToken") String fcmToken);
    int saveNotification(@Param("userCode") Long userCode, @Param("notiChat") boolean notiChat, @Param("notiReq") boolean notiReq);
    int save(User user);
}

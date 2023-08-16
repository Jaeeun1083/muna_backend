package com.life.muna.user.dto.info;

import com.life.muna.user.domain.User;
import com.life.muna.user.domain.enums.UserLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserLevelResponse {
    private UserLevel userLevel;
    private int levelMaxCnt;
    private int historyCnt;

    @Builder
    private UserLevelResponse(UserLevel userLevel, int levelMaxCnt, int historyCnt) {
        this.userLevel = userLevel;
        this.levelMaxCnt = levelMaxCnt;
        this.historyCnt = historyCnt;
    }

    public static UserLevelResponse of(User user, int historyCnt) {
//        int reqCnt = user.getUserLevel().requiredNanumCnt() - historyCnt;
        return UserLevelResponse.builder()
                .userLevel(user.getUserLevel())
//                .reqCnt(user.getUserLevel().getNanumCnt() - historyCnt)
//                .levelMaxCnt(Math.max(reqCnt, 0))
                .levelMaxCnt(user.getUserLevel().requiredNanumCnt())
                .historyCnt(historyCnt)
                .build();
    }

}

package com.life.muna.user.service;

import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.common.util.SmsSender;
import com.life.muna.user.domain.TempKey;
import com.life.muna.user.dto.signUp.IssueTempKeyRequest;
import com.life.muna.user.dto.signUp.IssueTempKeyResponse;
import com.life.muna.user.dto.signUp.VerifyTempKeyRequest;
import com.life.muna.user.mapper.TempKeyMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.life.muna.user.domain.TempKey.createTempKey;

@Service
public class TempkeyService {
    private final TempKeyMapper tempKeyMapper;
    private final static int MAX_ISSUED = 5;
    private final static long POSSIBLE_REISSUE_SECOND = 30;

    public TempkeyService(TempKeyMapper tempKeyMapper) {
        this.tempKeyMapper = tempKeyMapper;
    }

    public IssueTempKeyResponse issueTempKey(IssueTempKeyRequest issueTempKeyRequest) {
        Optional<TempKey> tempKeyOptional = tempKeyMapper.findByPhone(issueTempKeyRequest.getPhone());
        TempKey tempKey;

        int randomValue = generateRandomValue();
        String msg = "[무나] 본인확인 인증번호(" + randomValue + ") 입력시 정상처리 됩니다.";

        // tempKey를 발급 받은 적이 있는 경우
        if (tempKeyOptional.isPresent()) {
            tempKey = tempKeyOptional.get();
            LocalDateTime today = LocalDateTime.now(); // 오늘
            if (tempKey.getUpdateDate().toLocalDate().isBefore(today.toLocalDate())) {
                //발급 날짜가 어제인 경우
                SmsSender.messageSend(issueTempKeyRequest.getPhone(), "본인인증", msg);
                tempKey.updateValue(randomValue, 1);
            } else {
                // 하루 발급 횟수가 MAX_ISSUED 경우
                if (tempKey.getIssuedCnt() >= MAX_ISSUED) throw new BusinessException(ErrorCode.EXCEED_ISSUED_TEMPKEY_COUNT);

                Duration duration = Duration.between(tempKey.getUpdateDate(), today);
                long secondsDifference = Math.abs(duration.getSeconds());

                // 발급 받은지 30초 이내일 경우
                if(secondsDifference <= POSSIBLE_REISSUE_SECOND) {
                    tempKey.updateValue(0, tempKey.getIssuedCnt());
                    return IssueTempKeyResponse.of(tempKey, MAX_ISSUED - tempKey.getIssuedCnt(), POSSIBLE_REISSUE_SECOND - secondsDifference);
                }
                SmsSender.messageSend(issueTempKeyRequest.getPhone(), "본인인증", msg);
                tempKey.updateValue(randomValue, tempKey.getIssuedCnt() + 1);
            }
            int result = tempKeyMapper.updateTempKey(tempKey);
            if (result != 1) throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        } else {
            // temp key 발급을 한 적이 없는 경우
            tempKey = createTempKey(randomValue, issueTempKeyRequest.getPhone());
            SmsSender.messageSend(issueTempKeyRequest.getPhone(), "본인인증", msg);
            tempKeyMapper.save(tempKey);
        }

        return IssueTempKeyResponse.of(tempKey, MAX_ISSUED - tempKey.getIssuedCnt(), 0L);
    }

    public boolean verifyTempKey(VerifyTempKeyRequest verifyTempKeyRequest) {
        TempKey tempKey = tempKeyMapper.findByPhone(verifyTempKeyRequest.getPhone())
                .filter(key -> !key.isVerified())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_TEMPKEY));

        if (tempKey.getTempKey().equals(verifyTempKeyRequest.getTempKey())) {
            tempKey.verify();
            int result = tempKeyMapper.updateTempKey(tempKey);
            return result == 1;
        }
        return false;
    }

    private int generateRandomValue() {
        return (int)(Math.random() * (99999 - 10000 + 1) + 10000);
    }

}

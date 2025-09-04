package com.ssafy.a705.domain.verification.repository;

import com.ssafy.a705.domain.verification.exception.TooManySmsRequestsException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SmsRepository {

    @Value("${coolsms.limitTime}")
    private int SMS_LIMIT_TIME;

    @Value("${coolsms.limitRequestCnt}")
    private int SMS_REQUEST_LIMIT_CNT;

    @Value("${coolsms.limitRequestTime}")
    private int SMS_REQUEST_LIMIT_TIME;


    private static final String SMS_PREFIX = "SMS:";
    private static final String LIMIT_PREFIX = "SMS-LIMIT:";

    private final StringRedisTemplate redisTemplate;

    public void createSmsCertification(String phone, String code) {
        String verifyKey = SMS_PREFIX + phone;
        String limitKey = LIMIT_PREFIX + phone;

        Long count = redisTemplate.opsForValue().increment(limitKey);
        if (count == 1) {
            redisTemplate.expire(limitKey, Duration.ofSeconds(SMS_REQUEST_LIMIT_TIME));
        } else if (count > SMS_REQUEST_LIMIT_CNT) {
            throw new TooManySmsRequestsException();
        }

        redisTemplate.opsForValue().set(verifyKey, code, Duration.ofSeconds(SMS_LIMIT_TIME));
    }

    public String getSmsCertification(String phone) {
        return redisTemplate.opsForValue().get(SMS_PREFIX + phone);
    }

    public void deleteSmsCertification(String phone) {
        redisTemplate.delete(SMS_PREFIX + phone);
    }

    public boolean hasKey(String phone) {
        return redisTemplate.hasKey(SMS_PREFIX + phone);
    }
}
package com.ssafy.a705.global.security.logout.service;

import com.ssafy.a705.global.security.logout.repository.BlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;

    public void block(String jti, long ttlSeconds) {
        blacklistRepository.deleteTokenByEmail(jti, ttlSeconds);
    }

    public boolean isBlocked(String jti) {
        return Boolean.TRUE.equals(blacklistRepository.hasKey(jti));
    }
}

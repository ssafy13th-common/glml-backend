package com.ssafy.a705.domain.member._auth.service;

import com.ssafy.a705.domain.member._auth.dto.request.KakaoTokenReq;
import com.ssafy.a705.domain.member._auth.dto.response.KakaoProfileRes;
import com.ssafy.a705.domain.member._auth.exception.MissingKakaoProfileException;
import com.ssafy.a705.domain.member.dto.TokenRes;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.entity.Role;
import com.ssafy.a705.domain.member.entity.SocialType;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.common.redis.RedisService;
import com.ssafy.a705.global.security.jwt.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public KakaoProfileRes getProfile(KakaoTokenReq request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(request.accessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 토큰 기반으로 정보 불러오기
        ResponseEntity<KakaoProfileRes> resp = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                KakaoProfileRes.class
        );

        KakaoProfileRes profileRes = resp.getBody();
        if (profileRes == null || profileRes.id() == null) {
            throw new MissingKakaoProfileException();
        }

        return profileRes;
    }

    public TokenRes exchangeTokens(KakaoTokenReq request) {
        KakaoProfileRes profileRes = getProfile(request);

        Member member = memberRepository.findByEmailAndDeletedAtIsNull(profileRes.email())
                .orElseGet(() ->
                        memberRepository.save(
                                Member.of(profileRes.nickname(), profileRes.profileImage(),
                                        profileRes.email(), Role.GUEST, SocialType.KAKAO))
                );

        // 추가정보 입력 후 저장
        member.updateAdditionalInfo(request.name(), request.gender());

        // 토큰 발급
        String accessToken = jwtProvider.createAccessToken(profileRes.email());
        String refreshToken = jwtProvider.createRefreshToken(profileRes.email());

        redisService.saveRefreshToken(
                member.getEmail(),
                refreshToken,
                jwtProvider.getRefreshTokenExpirationPeriod()
        );

        return TokenRes.of(accessToken, refreshToken);
    }
}

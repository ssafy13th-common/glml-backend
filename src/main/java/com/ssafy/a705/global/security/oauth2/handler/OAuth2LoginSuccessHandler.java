package com.ssafy.a705.global.security.oauth2.handler;

import com.ssafy.a705.domain.member.dto.TokenRes;
import com.ssafy.a705.global.common.redis.RedisService;
import com.ssafy.a705.global.security.jwt.service.JwtProvider;
import com.ssafy.a705.global.security.oauth2.CustomOAuth2Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    // 나중에 시간나면 OAuth2 인증 정보 Session에 저장하고,
    // 토큰 없이 추가 정보 입력 페이지로 redirect 후 추가정보 입력이 완료되어 가입 완료가 되는 시점에 토큰 발급으로 바꿀것
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        log.info("OAuth2 Login 성공!");

        try {
            CustomOAuth2Member oAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();

            loginSuccess(response, oAuth2Member);

            /* 현재 사용하지 않는 login 로직 주석 처리
            if (oAuth2Member.getRole() == Role.GUEST) {
                String accessToken = jwtProvider.createAccessToken(oAuth2Member.getEmail());
                String refreshToken = jwtProvider.createRefreshToken();

                response.addHeader(jwtProvider.getAccessHeader(), "Bearer " + accessToken);
                response.sendRedirect("oauth2/sign-up"); // 프론트의 추가 정보 입력 폼으로 리다이렉트

                TokenRes tokenRes = TokenRes.of(accessToken, null);
                jwtProvider.sendTokensInResponse(response, tokenRes);
            } else {
                loginSuccess(response, oAuth2Member); // 로그인에 성공한 경우, access, refreash 토큰 생성
            }*/
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 임시 - 로그인 할 때마다 AccessToken과 RefreshToken을 발급하고, Header에 담아 보냄
     *
     * @param response
     * @param oAuth2Member
     * @throws IOException
     */
    private void loginSuccess(HttpServletResponse response, CustomOAuth2Member oAuth2Member)
            throws IOException {
        String accessToken = jwtProvider.createAccessToken(oAuth2Member.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(oAuth2Member.getEmail());

        redisService.saveRefreshToken(oAuth2Member.getEmail(), refreshToken,
                jwtProvider.getRefreshTokenExpirationPeriod());

        TokenRes tokenRes = TokenRes.of(accessToken, refreshToken);
        jwtProvider.sendTokensInResponse(response, tokenRes);
    }

    /*
    * 추후 이것으로 변경할 것 - 로그인 시 RefreshToken의 유효성을 체크하고, 유효하면 그대로 사용, 없거나 만료되었다면 RefreshToken을 새로 발급하는 방식
    private void loginSuccess(HttpServletResponse response, CustomOAuth2Member oAuth2Member)
            throws IOException {
        String accessToken = jwtProvider.createAccessToken(oAuth2Member.getEmail());
        String savedRefreshToken = refreshTokenRepository.findByEmail(oAuth2Member.getEmail());
        String refreshToken;

        if(savedRefreshToken != null && jwtProvider.isTokenValid(savedRefreshToken)){
            refreshToken = savedRefreshToken;
        } else {
            refreshToken = jwtProvider.createRefreshToken();
            refreshTokenRepository.save(email, refreshToken);
        }

        response.addHeader(jwtProvider.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtProvider.getRefreshHeader(), "Bearer " + refreashToken);

        jwtProvider.sendAccessTokenAndRefreshToken(response, accessToken, refreashToken);
    }
    */

}

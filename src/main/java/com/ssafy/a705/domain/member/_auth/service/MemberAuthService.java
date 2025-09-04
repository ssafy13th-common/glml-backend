package com.ssafy.a705.domain.member._auth.service;

import com.ssafy.a705.domain.member._auth.dto.request.SignUpReq;
import com.ssafy.a705.domain.member._auth.exception.DuplicatedMemberException;
import com.ssafy.a705.domain.member._auth.exception.IlleagalVerificationException;
import com.ssafy.a705.domain.member._auth.exception.InvalidTokenException;
import com.ssafy.a705.domain.member._auth.exception.VerifiedEmailException;
import com.ssafy.a705.domain.member._auth.repository.MemberAuthRepository;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.entity.Role;
import com.ssafy.a705.domain.member.entity.SocialType;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.common.redis.RedisService;
import com.ssafy.a705.global.security.jwt.service.JwtProvider;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.global.security.logout.service.BlacklistService;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberAuthRepository authRepository;
    private final JavaMailSender mailSender;
    private final JwtProvider jwtProvider;
    private final BlacklistService blacklistService;
    private final RedisService redisService;

    @Transactional
    public void signUp(SignUpReq request, String baseUrl) {
        String email = request.email().trim().toLowerCase();

        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedMemberException();
        }

        Member member = Member.of(request.name(), request.nickname(), email,
                passwordEncoder.encode(request.password()), request.gender(),
                request.profileImage(),
                Role.USER, SocialType.LOCAL);
        memberRepository.save(member);
        issueAndVerification(member, baseUrl);
    }

    @Transactional
    public void resendVerification(String email, String baseUrl) {

        email = email.trim().toLowerCase();
        log.info("[RESEND] 요청 email={}", email);
        Member m = memberRepository.getByEmail(email);

        if (email == null || email.isBlank()) {
            log.warn("[RESEND] 이메일 누락");
            throw new IllegalArgumentException("이메일이 필요합니다.");
        }

        if (m.isEmailVerified()) {
            throw new VerifiedEmailException();
        }

        String rlKey = "rate:verify:" + email;
        Long cnt = authRepository.getRate(email);
        if (cnt != null && cnt == 1L) {
            authRepository.setRateLimit(email);
        }
        log.info("[RESEND] rate={}/5, key={}", cnt, rlKey);
        if (cnt != null && cnt > 5) {
            log.warn("[RESEND] 레이트리밋 초과 email={}", email);
            throw new IlleagalVerificationException();
        }

        String oldToken = authRepository.getToken(m.getId());
        if (oldToken != null) {
            authRepository.deleteToken(oldToken);
            log.info("[RESEND] 이전 토큰 삭제: {}", oldToken);
        }

        issueAndVerification(m, baseUrl);
    }

    @Transactional
    public void verify(String token) {
        String memberIdStr = authRepository.getMemberIdStr(token);
        if (memberIdStr == null) {
            throw new InvalidTokenException();
        }

        Long memberId = Long.valueOf(memberIdStr);
        Member m = memberRepository.getById(memberId);
        log.info("member found : {}, member verified email? : {}", m.getEmail(),
                m.isEmailVerified());
        if (m.isEmailVerified()) {
            authRepository.cleanupToken(memberId, token);
            throw new VerifiedEmailException();
        }

        m.updateIsEmailVerified();
        authRepository.cleanupToken(memberId, token);
        log.info("email verification complete!");
    }

    private String generateToken() {
        byte[] buf = new byte[32];
        new SecureRandom().nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }


    private void issueAndVerification(Member m, String baseUrl) {
        String token = generateToken();
        authRepository.createVerificationToken(m.getId(), token);

        String link = baseUrl + URLEncoder.encode(token, StandardCharsets.UTF_8);
        sendEmail(m.getEmail(), m.getNickname(), link);
    }

    private void sendEmail(String to, String nickname, String link) {
        log.info("[MAIL] 전송 시도 to={}, link={}", to, link);
        try {
            MimeMessage mm = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mm, "UTF-8");
            helper.setTo(to);
            helper.setSubject("이메일 인증을 완료해주세요");

            String html = """
                    <div style="font-family:sans-serif">
                            <h2>안녕하세요, %s님</h2>
                            <p>아래 버튼을 눌러 이메일 인증을 완료해주세요. (유효기간: 5분)</p>
                          
                            <!-- Bulletproof button (table + a) -->
                            <table role="presentation" cellspacing="0" cellpadding="0" border="0" style="border-collapse:separate;">
                              <tr>
                                <td align="center" bgcolor="#4f46e5" style="border-radius:6px;">
                                  <a href="%s"
                                     target="_blank"
                                     style="display:inline-block;padding:10px 16px;border-radius:6px;background:#4f46e5;
                                            color:#ffffff;text-decoration:none;font-weight:600;line-height:1;font-family:Arial,sans-serif;">
                                    이메일 인증
                                  </a>
                                </td>
                              </tr>
                            </table>
                          
                            <!-- 텍스트 링크 백업 -->
                            <p style="font-size:12px;color:#6b7280;margin-top:12px;">
                              버튼이 동작하지 않으면 다음 링크를 브라우저에 복사해 붙여넣어 주세요:<br>
                              <span style="word-break:break-all;">%s</span>
                            </p>
                          </div>
                    """.formatted(nickname == null ? "" : nickname, link, link);

            helper.setText(html, true);
            mailSender.send(mm);
            log.info("[MAIL] 전송 완료 to={}", to);
        } catch (Exception e) {
            log.error("[MAIL] 전송 실패 to={} / message={}", to, e.getMessage(), e);
            throw new IllegalStateException("메일 전송에 실패했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    @Transactional
    public void delete(CustomUserDetails customUserDetails, HttpServletRequest request) {
        Member member = memberRepository.getByEmail(customUserDetails.getEmail());
        member.deleteMember();
        // 토큰 삭제
        String accessToken = jwtProvider.extractAccessToken(request).orElse(null);

        // refreshToken 삭제
        redisService.deleteRefreshToken(customUserDetails.getEmail());

        if (accessToken != null && jwtProvider.isTokenValid(accessToken)) {
            // accessToken 블랙리스트에 등록(더이상 사용 못하도록)
            String jti = jwtProvider.getJti(accessToken);
            long ttlSeconds = jwtProvider.getRemainingSeconds(accessToken);
            blacklistService.block(jti, ttlSeconds);
        }

    }
}

package com.ssafy.a705.global.security.oauth2;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.entity.Role;
import com.ssafy.a705.domain.member.entity.SocialType;
import com.ssafy.a705.global.security.oauth2.entity.KakaoOAuth2Member;
import com.ssafy.a705.global.security.oauth2.entity.OAuth2Member;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

    private String nameAttributeKey; // OAuth 로그인 진행 시 키가 되는 필드값, PK와 같은 의미
    private OAuth2Member oAuth2Member; // 소셜 타입 별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등)

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2Member oAuth2Member) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2Member = oAuth2Member;
    }

    /**
     * @param socialType            OAuth2 로그인 시 사용되는 소셜 타입
     * @param userNameAttributeName OAuth2 로그인 시 키(PK)가 되는 값
     * @param attributes            OAuth2 서비스의 유저 정보들
     * @return SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환 회원의 식별값(id), attributes,
     * nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName,
            Map<String, Object> attributes) {
        /*
        * 추후 확장을 위함 - socialType으로 구분
        if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
         */
        return ofKakao(userNameAttributeName, attributes); // 현재는 OAuth2 로그인이 카카오밖에 없으므로 카카오 return
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName,
            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2Member(new KakaoOAuth2Member(attributes))
                .build();
    }

    /**
     * @param socialType   소셜 로그인 타입(현재는 KAKAO, LOCAL 두 개만 있음)
     * @param oauth2Member 닉네임, 프로필 이미지 url, email 주소를 담고 있는 oAuth2Member 객체
     * @return 소셜 로그인을 통해 넘겨받은 정보로 GUEST role을 부여한 Member 객체 소셜 로그인을 통해 넘겨받은 정보로 GUEST role을 부여한
     * Member 객체 생성 후, 추가 정보 입력 페이지에서 닉네임(선택), 프로필 이미지(선택), 이름, 성별을 입력하고 나서 role을 USER로 변경
     */
    public Member toEntity(SocialType socialType, OAuth2Member oauth2Member) {
        /**
         * 소셜 로그인을 통해 넘겨받은 정보로 GUEST role을 부여한 Member 객체 생성
         * 이후, 추가 정보 입력 페이지에서 닉네임(선택), 프로필 이미지(선택), 이름, 성별을 입력하고 나서
         * role을 USER로 변경
         */
        return Member.of(oauth2Member.getNickname(), oauth2Member.getImageUrl(),
                oauth2Member.getEmail(), Role.GUEST, socialType);
    }
}

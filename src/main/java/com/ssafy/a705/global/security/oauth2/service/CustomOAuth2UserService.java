package com.ssafy.a705.global.security.oauth2.service;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.entity.SocialType;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.security.oauth2.CustomOAuth2Member;
import com.ssafy.a705.global.security.oauth2.OAuthAttributes;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements
        OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        /**
         * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
         * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공
         * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
         * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
         * userRequest에서 registrationId 추출 후 registrationId로 SocialType 저장
         * http://localhost:8080/v1/oauth2/kakao 에서 kakao가 registrationId
         * userNameAttributeName은 이후에 nameAttributeKey로 설정
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json값(유저 정보들)

        // socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName,
                attributes);
        Member createdMember = getMember(extractAttributes,
                socialType); // getMember() 메서드로 Member 객체 생성 후 반환

        // DefaultOAuth2User를 구현한 CustomOAuth2Member 객체를 생성해서 반환
        return new CustomOAuth2Member(
                Collections.singleton(
                        new SimpleGrantedAuthority(createdMember.getRole().name())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdMember.getEmail(),
                createdMember.getRole()
        );
    }

    /**
     * registrationId를 활용해 소셜 타입 판별
     *
     * @param registrationId userRequest에서 추출한 registrationId
     * @return Social Type
     */
    private SocialType getSocialType(String registrationId) {
        // 추후 확장을 위함
//        if (KAKAO.equals((registrationId))) {
//            return SocialType.KAKAO;
//        }
        return SocialType.KAKAO; // 지금은 소셜 로그인이 카카오밖에 없으므로 카카오 return
    }

    /**
     * Social Type과 Social Id로 멤버 검색 후 반환. 없다면 해당 정보를 바탕으로 멤버 객체 생성 후 영속성 컨텍스트에 저장 및 반환
     *
     * @param attributes 소셜 로그인 API를 통해 받아온 유저 정보
     * @param socialType 소셜 타입
     * @return Social Type과 Social Id가 일치하는 멤버 객체 혹은 받아온 유저 정보로 생성한 객체
     */
    private Member getMember(OAuthAttributes attributes, SocialType socialType) {
        Member findMember = (Member) memberRepository.findByEmailAndDeletedAtIsNull(
                attributes.getOAuth2Member().getEmail()).orElse(null);

        if (findMember == null) {
            return saveMember(attributes, socialType);
        }
        return findMember;
    }

    /**
     * API로 받은 소셜 로그인 유저 정보를 바탕으로 멤버 객체 생성 후, 영속성 컨텍스트에 저장 및 반환 생성된 객체는 socialType, socialId, email,
     * role, profile image url만 있는 상태
     *
     * @param attributes 소셜 로그인 API를 통해 받아온 유저 정보
     * @param socialType 소셜 타입
     * @return attributes 내의 정보들과 socialType을 바탕으로 생성한 Member 객체
     */
    private Member saveMember(OAuthAttributes attributes, SocialType socialType) {
        Member createdMember = attributes.toEntity(socialType, attributes.getOAuth2Member());
        return memberRepository.save(createdMember);
    }
}

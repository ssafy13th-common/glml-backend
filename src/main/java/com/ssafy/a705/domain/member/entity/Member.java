package com.ssafy.a705.domain.member.entity;

import com.ssafy.a705.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @Comment("회원 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("회원 이름")
    @Column(nullable = false, length = 50)
    private String name;

    @Comment("회원 닉네임")
    @Column(nullable = false, length = 20)
    private String nickname;

    @Comment("회원 이메일")
    @Column(nullable = false, unique = true)
    private String email;

    @Comment("회원 비밀번호")
    private String password;

    @Comment("회원 성별")
    @Column(nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    
    @Lob
    @Comment("회원 프로필 이미지 url")
    private String profileUrl;

    @Comment("회원 역할")
    @Column(nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Comment("회원 로그인 방식")
    @Column(nullable = false, length = 5)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Comment("회원 이메일 인증 여부")
    @Column(nullable = false)
    private boolean emailVerified;

    private Member(String name, String nickname, String email, String password, Gender gender,
            String profileUrl, Role role, SocialType socialType) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.profileUrl = profileUrl;
        this.role = role;
        this.socialType = socialType;
        this.emailVerified = false;
    }

    public static Member of(String name, String nickname, String email, String password,
            Gender gender, String profileUrl, Role role, SocialType socialType) {
        return new Member(name, nickname, email, password, gender, profileUrl, role, socialType);
    }

    // 추가 입력 정보 (이름, 성별)를 제외하고, OAuth를 통해 받아오는 정보들로 Member 객체 생성
    // 생성 시 Role을 Guest로 부여하고, 추가 정보 입력 페이지에서 추가 정보를 입력하고 나면 USER로 role 변경
    private Member(String nickname, String imageUrl, String email, Role role,
            SocialType socialType) {
        this.nickname = nickname;
        this.profileUrl = imageUrl;
        this.email = email;
        this.role = role;
        this.socialType = socialType;
        // 임시
        this.name = "guest";
        this.gender = Gender.MALE;
        this.emailVerified = false;
    }

    public static Member of(String nickname, String imageUrl, String email, Role role,
            SocialType socialType) {
        return new Member(nickname, imageUrl, email, role, socialType);
    }

    public void updateIsEmailVerified() {
        this.emailVerified = true;
    }

    public void updateAdditionalInfo(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
        this.role = Role.USER;
    }

    public void deleteMember() {
        this.delete(LocalDateTime.now());
        this.nickname = "deleted_user";
        this.name = "deleted_user";
        this.email = "deleted_" + this.email;
    }

    public void updateProfileUrl(String url) {
        this.profileUrl = url;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
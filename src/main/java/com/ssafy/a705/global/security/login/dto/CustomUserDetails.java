package com.ssafy.a705.global.security.login.dto;

import com.ssafy.a705.domain.member.entity.Member;
import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String password;
    private final String role;

    private CustomUserDetails(Long id, String email, String nickname, String password,
            String role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    public static CustomUserDetails of(Member member) {
        return new CustomUserDetails(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getPassword(),
                member.getRole().name()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

}

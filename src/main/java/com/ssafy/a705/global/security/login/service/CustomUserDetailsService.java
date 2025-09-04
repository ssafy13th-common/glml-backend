package com.ssafy.a705.global.security.login.service;

import com.ssafy.a705.domain.member._auth.exception.NotVerifiedEmailException;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.entity.SocialType;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String emailLower = email.trim().toLowerCase();
        Member member = memberRepository.getByEmail(emailLower);
        if (member.getSocialType().equals(SocialType.LOCAL) && !member.isEmailVerified()) {
            throw new NotVerifiedEmailException();
        }
        return CustomUserDetails.of(member);
    }
}

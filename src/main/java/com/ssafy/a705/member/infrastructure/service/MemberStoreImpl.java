package com.ssafy.a705.member.infrastructure.service;

import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.member.domain.repository.MemberRepository;
import com.ssafy.a705.member.domain.service.MemberStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberStoreImpl implements MemberStore {

    private final MemberRepository memberRepository;

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }
}

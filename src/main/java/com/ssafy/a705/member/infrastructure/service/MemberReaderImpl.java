package com.ssafy.a705.member.infrastructure.service;

import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.member.domain.exception.DuplicatedEmailException;
import com.ssafy.a705.member.domain.exception.MemberNotFoundException;
import com.ssafy.a705.member.domain.repository.MemberRepository;
import com.ssafy.a705.member.domain.service.MemberReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberReaderImpl implements MemberReader {

    private final MemberRepository memberRepository;

    @Override
    public Member getMember(String email) {
        return memberRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public List<Member> getAllMembers(List<String> emails) {
        List<Member> members = memberRepository.findByEmailInAndDeletedAtIsNull(emails);
        if (members.isEmpty()) {
            throw new MemberNotFoundException();
        }
        return members;
    }

    @Override
    public List<Member> getAllMembersByNickname(String nickname) {
        return memberRepository.findAllByNicknameNotDeleted(nickname);
    }

    @Override
    public void checkEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedEmailException();
        }
    }
}

package com.ssafy.a705.member.domain.service;

import com.ssafy.a705.member.domain.entity.Member;
import java.util.List;

public interface MemberReader {

    Member getMember(String email);

    List<Member> getAllMembers(List<String> emails);

    List<Member> getAllMembersByNickname(String nickname);

    void checkEmail(String email);
}

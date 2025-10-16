package com.ssafy.a705.member.domain.service;

import com.ssafy.a705.member.domain.entity.Member;

public interface MemberImageService {

    String getUrl(String url);

    String generateUrl(Member member);
}

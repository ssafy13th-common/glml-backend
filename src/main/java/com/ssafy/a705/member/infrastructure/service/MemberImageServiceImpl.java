package com.ssafy.a705.member.infrastructure.service;

import com.ssafy.a705.global.common.utils.S3PresignedUrlGenerator;
import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.member.domain.service.MemberImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberImageServiceImpl implements MemberImageService {

    private final S3PresignedUrlGenerator uploader;

    @Override
    public String getUrl(String url) {
        if (url.startsWith("members/")) {
            return uploader.generatePresignedGetUrl(url);
        }
        return url;
    }

    @Override
    public String generateUrl(Member member) {
        String memberProfile = member.getProfileUrl();
        if (memberProfile.startsWith("members/")) {
            return uploader.generatePresignedGetUrl(memberProfile);
        }
        return memberProfile;
    }
}

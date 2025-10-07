package com.ssafy.a705.domain.member.service;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.board.domain.service.PostReader;
import com.ssafy.a705.board.domain.service.ReplyReader;
import com.ssafy.a705.domain.member.dto.request.UpdateNicknameReq;
import com.ssafy.a705.domain.member.dto.request.UpdateProfileReq;
import com.ssafy.a705.domain.member.dto.response.MemberDetailRes;
import com.ssafy.a705.domain.member.dto.response.MemberInfosRes;
import com.ssafy.a705.domain.member.dto.response.MemberPostsRes;
import com.ssafy.a705.domain.member.dto.response.MemberRepliesRes;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.exception.DuplicatedEmailException;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.common.utils.S3PresignedUrlGenerator;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PostReader postService;
    private final S3PresignedUrlGenerator uploader;
    private final ReplyReader replyService;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberPostsRes getMemberBoards(CustomUserDetails userDetails, Pageable pageable) {
        Member member = memberRepository.getById(userDetails.getId());
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("id").descending());
        Page<Post> boards = postService.getMemberPost(member, pageable);
        return MemberPostsRes.from(boards);
    }

    @Transactional(readOnly = true)
    public MemberRepliesRes getMemberReplies(CustomUserDetails userDetails, Pageable pageable) {
        Member member = memberRepository.getById(userDetails.getId());
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("id").descending());
        Page<Reply> replies = replyService.getMemberReplies(member, pageable);
        return MemberRepliesRes.from(replies);
    }

    @Transactional(readOnly = true)
    public MemberInfosRes findMember(String search, CustomUserDetails userDetails) {
        List<Member> members = memberRepository.findAllByNicknameNotDeleted(search);
        return MemberInfosRes.of(members);
    }

    @Transactional(readOnly = true)
    public MemberDetailRes getMemberDetail(CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        String profileUrl = member.getProfileUrl();
        if (member.getProfileUrl().startsWith("members/")) {
            profileUrl = uploader.generatePresignedGetUrl(profileUrl);
        }
        return MemberDetailRes.from(member, profileUrl);
    }

    @Transactional
    public void updateProfile(UpdateProfileReq profileReq, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        checkMember(userDetails, profileReq.email());
        member.updateProfileUrl(profileReq.profileUrl());
    }

    @Transactional
    public void updateNickname(UpdateNicknameReq nicknameReq, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        checkMember(userDetails, nicknameReq.email());
        member.updateNickname(nicknameReq.nickname());
    }

    public String getUrl(String url) {
        if (url.startsWith("members/")) {
            return uploader.generatePresignedGetUrl(url);
        }
        return url;
    }

    private void checkMember(CustomUserDetails userDetails, String email) {
        if (Objects.equals(userDetails.getEmail(), email)) {
            return;
        }
        throw new ForbiddenException("회원 정보 접근");
    }

    @Transactional(readOnly = true)
    public void checkEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedEmailException();
        }
    }
}

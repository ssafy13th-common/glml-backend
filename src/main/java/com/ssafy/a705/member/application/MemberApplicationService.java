package com.ssafy.a705.member.application;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.board.domain.service.PostReader;
import com.ssafy.a705.board.domain.service.ReplyReader;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.member.domain.service.MemberImageService;
import com.ssafy.a705.member.domain.service.MemberReader;
import com.ssafy.a705.member.domain.service.MemberService;
import com.ssafy.a705.member.presentation.dto.request.UpdateNicknameReq;
import com.ssafy.a705.member.presentation.dto.request.UpdateProfileReq;
import com.ssafy.a705.member.presentation.dto.response.MemberDetailRes;
import com.ssafy.a705.member.presentation.dto.response.MemberInfosRes;
import com.ssafy.a705.member.presentation.dto.response.MemberPostsRes;
import com.ssafy.a705.member.presentation.dto.response.MemberRepliesRes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberApplicationService {

    private final PostReader postService;
    private final ReplyReader replyService;
    private final MemberReader memberReader;
    private final MemberService memberService;
    private final MemberImageService imageService;

    @Transactional(readOnly = true)
    public MemberPostsRes getMemberBoards(CustomUserDetails userDetails, Pageable pageable) {
        Member member = memberService.getMember(userDetails.getEmail());
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("id").descending());
        Page<Post> boards = postService.getMemberPost(member, pageable);
        return MemberPostsRes.from(boards);
    }

    @Transactional(readOnly = true)
    public MemberRepliesRes getMemberReplies(CustomUserDetails userDetails, Pageable pageable) {
        Member member = memberService.getMember(userDetails.getEmail());
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("id").descending());
        Page<Reply> replies = replyService.getMemberReplies(member, pageable);
        return MemberRepliesRes.from(replies);
    }

    @Transactional(readOnly = true)
    public MemberInfosRes findMember(String search, CustomUserDetails userDetails) {
        List<Member> members = memberReader.getAllMembersByNickname(search);
        return MemberInfosRes.of(members);
    }

    @Transactional(readOnly = true)
    public MemberDetailRes getMemberDetail(CustomUserDetails userDetails) {
        Member member = memberService.getMember(userDetails.getEmail());
        String profileUrl = imageService.generateUrl(member);
        return MemberDetailRes.from(member, profileUrl);
    }

    @Transactional
    public void updateProfile(UpdateProfileReq profileReq, CustomUserDetails userDetails) {
        Member member = memberService.getMember(userDetails.getEmail());
        memberService.checkMember(userDetails, profileReq.email());
        member.updateProfileUrl(profileReq.profileUrl());
    }

    @Transactional
    public void updateNickname(UpdateNicknameReq nicknameReq, CustomUserDetails userDetails) {
        Member member = memberService.getMember(userDetails.getEmail());
        memberService.checkMember(userDetails, nicknameReq.email());
        member.updateNickname(nicknameReq.nickname());
    }

    @Transactional(readOnly = true)
    public void checkEmail(String email) {
        memberReader.checkEmail(email);
    }

}

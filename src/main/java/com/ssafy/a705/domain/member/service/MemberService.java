package com.ssafy.a705.domain.member.service;

import com.ssafy.a705.domain.board._comment.entity.CompanyComment;
import com.ssafy.a705.domain.board._comment.service.CommentService;
import com.ssafy.a705.domain.board.entity.CompanyBoard;
import com.ssafy.a705.domain.board.service.BoardService;
import com.ssafy.a705.domain.member.dto.request.UpdateNicknameReq;
import com.ssafy.a705.domain.member.dto.request.UpdateProfileReq;
import com.ssafy.a705.domain.member.dto.response.MemberBoardsRes;
import com.ssafy.a705.domain.member.dto.response.MemberCommentsRes;
import com.ssafy.a705.domain.member.dto.response.MemberDetailRes;
import com.ssafy.a705.domain.member.dto.response.MemberInfosRes;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.exception.DuplicatedEmailException;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.common.utils.S3PresignedUploader;
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

    private final BoardService boardService;
    private final S3PresignedUploader uploader;
    private final CommentService commentService;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberBoardsRes getMemberBoards(CustomUserDetails userDetails, Pageable pageable) {
        Member member = memberRepository.getById(userDetails.getId());
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("id").descending());
        Page<CompanyBoard> boards = boardService.getMemberBoard(member, pageable);
        return MemberBoardsRes.from(boards);
    }

    @Transactional(readOnly = true)
    public MemberCommentsRes getMemberComments(CustomUserDetails userDetails, Pageable pageable) {
        Member member = memberRepository.getById(userDetails.getId());
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("id").descending());
        Page<CompanyComment> comments = commentService.getMemberComments(member, pageable);
        return MemberCommentsRes.from(comments);
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

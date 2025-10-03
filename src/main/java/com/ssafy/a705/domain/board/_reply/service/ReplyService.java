package com.ssafy.a705.domain.board._reply.service;

import com.ssafy.a705.domain.board._reply.dto.request.ReplyRegisterReq;
import com.ssafy.a705.domain.board._reply.dto.request.ReplyUpdateReq;
import com.ssafy.a705.domain.board._reply.entity.Reply;
import com.ssafy.a705.domain.board._reply.repository.ReplyRepository;
import com.ssafy.a705.domain.board._post.entity.Post;
import com.ssafy.a705.domain.board._post.service.PostService;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final MemberRepository memberRepository;
    private final PostService postService;
    private final ReplyRepository replyRepository;

    @Transactional
    public void createReply(Long postId, ReplyRegisterReq replyReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = postService.getPostById(postId);
        Reply parent = replyRepository.findByIdAndReply(replyReq.parentId(),
                post).orElse(null);
        Reply reply = Reply.from(replyReq, post, member, parent);
        replyRepository.save(reply);
    }

    @Transactional
    public void updateReply(Long postId, Long replyId, ReplyUpdateReq replyUpdateReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = postService.getPostById(postId);
        Reply reply = replyRepository.getByIdAndReply(replyId, post);
        checkMemberCanEdit(member, reply);
        reply.updateContent(replyUpdateReq);
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = postService.getPostById(postId);
        Reply reply = replyRepository.getByIdAndReply(replyId, post);
        checkMemberCanEdit(member, reply);
        reply.deleteReply();
    }

    public Page<Reply> getMemberReplies(Member member, Pageable pageable) {
        return replyRepository.findAllByMemberNotDeleted(member, pageable);
    }

    private void checkMemberCanEdit(Member member, Reply reply) {
        if (!Objects.equals(member, reply.getMember())) {
            throw new ForbiddenException("댓글 접근");
        }
    }

}
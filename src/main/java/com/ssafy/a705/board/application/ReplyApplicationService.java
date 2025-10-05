package com.ssafy.a705.board.application;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.board.domain.service.PostService;
import com.ssafy.a705.board.domain.service.ReplyService;
import com.ssafy.a705.board.domain.service.ReplyStore;
import com.ssafy.a705.board.presentation.dto.request.ReplyRegisterReq;
import com.ssafy.a705.board.presentation.dto.request.ReplyUpdateReq;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyApplicationService {

    private final MemberRepository memberRepository;
    private final PostService postService;
    private final ReplyService replyService;
    private final ReplyStore replyStore;

    @Transactional
    public void createReply(Long postId, ReplyRegisterReq replyReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = postService.getPostById(postId);
        Reply parent = replyService.findParent(replyReq.parentId(), post);
        Reply reply = Reply.from(replyReq, post, member, parent);
        replyStore.saveReply(reply);
    }

    @Transactional
    public void updateReply(Long postId, Long replyId, ReplyUpdateReq replyUpdateReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = postService.getPostById(postId);
        Reply reply = replyService.getReply(replyId, post);
        replyService.checkMemberCanEdit(member, reply);
        reply.updateContent(replyUpdateReq);
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Post post = postService.getPostById(postId);
        Reply reply = replyService.getReply(replyId, post);
        replyService.checkMemberCanEdit(member, reply);
        reply.deleteReply();
    }


}
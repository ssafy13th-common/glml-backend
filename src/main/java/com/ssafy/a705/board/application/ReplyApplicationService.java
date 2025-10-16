package com.ssafy.a705.board.application;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.board.domain.service.PostReader;
import com.ssafy.a705.board.domain.service.ReplyReader;
import com.ssafy.a705.board.domain.service.ReplyStore;
import com.ssafy.a705.board.presentation.dto.request.ReplyRegisterReq;
import com.ssafy.a705.board.presentation.dto.request.ReplyUpdateReq;
import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.member.domain.service.MemberService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyApplicationService {

    private final MemberService memberService;
    private final PostReader postService;
    private final ReplyReader replyService;
    private final ReplyStore replyStore;

    @Transactional
    public void createReply(Long postId, ReplyRegisterReq replyReq,
            CustomUserDetails userDetails) {
        Member member = memberService.getMember(userDetails.getEmail());
        Post post = postService.getPost(postId);
        Reply parent = replyService.findParent(replyReq.parentId(), post);
        Reply reply = Reply.from(replyReq, post, member, parent);
        replyStore.saveReply(reply);
    }

    @Transactional
    public void updateReply(Long postId, Long replyId, ReplyUpdateReq replyUpdateReq,
            CustomUserDetails userDetails) {
        Member member = memberService.getMember(userDetails.getEmail());
        Post post = postService.getPost(postId);
        Reply reply = replyService.getReply(replyId, post);
        checkMemberCanEdit(member, reply);
        reply.updateContent(replyUpdateReq);
    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, CustomUserDetails userDetails) {
        Member member = memberService.getMember(userDetails.getEmail());
        Post post = postService.getPost(postId);
        Reply reply = replyService.getReply(replyId, post);
        checkMemberCanEdit(member, reply);
        reply.deleteReply();
    }

    private void checkMemberCanEdit(Member member, Reply reply) {
        if (!Objects.equals(member, reply.getMember())) {
            throw new ForbiddenException("댓글 접근");
        }
    }


}
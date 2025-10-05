package com.ssafy.a705.board.domain.service;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.board.domain.repository.ReplyRepository;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.global.common.exception.ForbiddenException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    public Page<Reply> getMemberReplies(Member member, Pageable pageable) {
        return replyRepository.findAllByMemberNotDeleted(member, pageable);
    }

    public Reply findParent(Long parentId, Post post) {
        return replyRepository.findByIdAndReply(parentId, post).orElse(null);
    }

    public Reply getReply(Long replyId, Post post) {
        return replyRepository.getByIdAndReply(replyId, post);
    }

    public void checkMemberCanEdit(Member member, Reply reply) {
        if (!Objects.equals(member, reply.getMember())) {
            throw new ForbiddenException("댓글 접근");
        }
    }

}

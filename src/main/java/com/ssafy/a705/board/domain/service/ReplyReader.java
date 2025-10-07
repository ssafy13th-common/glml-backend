package com.ssafy.a705.board.domain.service;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.board.domain.repository.ReplyRepository;
import com.ssafy.a705.domain.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReplyReader {

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

    public List<Reply> getReplies(Post post) {
        return replyRepository.findAllByPostAndNotDeleted(post);
    }

}

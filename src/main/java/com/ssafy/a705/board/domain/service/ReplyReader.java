package com.ssafy.a705.board.domain.service;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.member.domain.entity.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyReader {

    Page<Reply> getMemberReplies(Member member, Pageable pageable);

    Reply findParent(Long parentId, Post post);

    Reply getReply(Long replyId, Post post);

    List<Reply> getReplies(Post post);

}

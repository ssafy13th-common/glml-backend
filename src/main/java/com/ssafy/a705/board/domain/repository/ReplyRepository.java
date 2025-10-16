package com.ssafy.a705.board.domain.repository;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.member.domain.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository {

    Reply save(Reply reply);

    List<Reply> findAllByPostAndNotDeleted(@Param("post") Post post);

    Optional<Reply> findByIdAndReply(@Param("replyId") Long replyId,
            @Param("post") Post post);

    Page<Reply> findAllByMemberNotDeleted(@Param("member") Member member, Pageable pageable);

    Reply getByIdAndReply(Long replyId, Post post);
}

package com.ssafy.a705.board.infrastructure.repository;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.entity.Reply;
import com.ssafy.a705.board.domain.exception.ReplyNotFoundException;
import com.ssafy.a705.board.domain.repository.ReplyRepository;
import com.ssafy.a705.member.domain.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long>, ReplyRepository {

    @Override
    @Query("SELECT r FROM Reply r WHERE r.post = :post AND r.deletedAt IS NULL")
    List<Reply> findAllByPostAndNotDeleted(@Param("post") Post post);


    @Override
    @Query("SELECT r FROM Reply r WHERE r.id = :replyId AND r.post = :post AND r.deletedAt IS NULL")
    Optional<Reply> findByIdAndReply(@Param("replyId") Long replyId,
            @Param("post") Post post);

    @Override
    @Query("SELECT r FROM Reply r WHERE r.member = :member AND r.deletedAt IS NULL")
    Page<Reply> findAllByMemberNotDeleted(@Param("member") Member member,
            Pageable pageable);

    @Override
    default Reply getByIdAndReply(Long replyId, Post post) {
        return findByIdAndReply(replyId, post).orElseThrow(ReplyNotFoundException::new);
    }
}
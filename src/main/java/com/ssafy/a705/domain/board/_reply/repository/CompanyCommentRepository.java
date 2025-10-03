package com.ssafy.a705.domain.board._reply.repository;

import com.ssafy.a705.domain.board._reply.entity.Reply;
import com.ssafy.a705.domain.board._reply.exception.CommentNotFoundException;
import com.ssafy.a705.domain.board._post.entity.Post;
import com.ssafy.a705.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyCommentRepository extends JpaRepository<Reply, Long> {

    @Query("SELECT c FROM Reply c WHERE c.post = :board AND c.deletedAt IS NULL")
    List<Reply> findAllByCompanyBoardAndNotDeleted(@Param("post") Post post);


    @Query("SELECT c FROM Reply c WHERE c.id = :commentId AND c.post = :board AND c.deletedAt IS NULL")
    Optional<Reply> findByIdAndCompanyBoard(@Param("commentId") Long commentId,
            @Param("board") Post board);

    @Query("SELECT c FROM Reply c WHERE c.member = :member AND c.deletedAt IS NULL")
    Page<Reply> findAllByMemberNotDeleted(@Param("member") Member member,
            Pageable pageable);

    default @NonNull Reply getByIdAndCompanyBoard(@NonNull Long commentId,
            @NonNull Post post) {
        return findByIdAndCompanyBoard(commentId, post).orElseThrow(CommentNotFoundException::new);
    }
}
package com.ssafy.a705.domain.board._comment.repository;

import com.ssafy.a705.domain.board._comment.entity.CompanyComment;
import com.ssafy.a705.domain.board._comment.exception.CommentNotFoundException;
import com.ssafy.a705.domain.board.entity.CompanyBoard;
import com.ssafy.a705.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyCommentRepository extends JpaRepository<CompanyComment, Long> {

    @Query("SELECT c FROM CompanyComment c WHERE c.companyBoard = :board AND c.deletedAt IS NULL")
    List<CompanyComment> findAllByCompanyBoardAndNotDeleted(@Param("board") CompanyBoard board);


    @Query("SELECT c FROM CompanyComment c WHERE c.id = :commentId AND c.companyBoard = :board AND c.deletedAt IS NULL")
    Optional<CompanyComment> findByIdAndCompanyBoard(@Param("commentId") Long commentId,
            @Param("board") CompanyBoard board);

    @Query("SELECT c FROM CompanyComment c WHERE c.member = :member AND c.deletedAt IS NULL")
    Page<CompanyComment> findAllByMemberNotDeleted(@Param("member") Member member,
            Pageable pageable);

    default @NonNull CompanyComment getByIdAndCompanyBoard(@NonNull Long commentId,
            @NonNull CompanyBoard board) {
        return findByIdAndCompanyBoard(commentId, board).orElseThrow(CommentNotFoundException::new);
    }
}
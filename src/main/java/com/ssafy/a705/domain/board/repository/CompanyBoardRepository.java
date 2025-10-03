package com.ssafy.a705.domain.board.repository;

import com.ssafy.a705.domain.board.entity.Post;
import com.ssafy.a705.domain.member.entity.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyBoardRepository extends JpaRepository<Post, Long> {

    @Query("""
                SELECT b FROM Post b
                WHERE b.deletedAt IS NULL
                  AND (:cursorId IS NULL OR b.id < :cursorId)
                ORDER BY b.id DESC
            """)
    List<Post> findAllNotDeleted(
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    @Query("SELECT b FROM Post b WHERE b.member = :member AND b.deletedAt IS NULL")
    Page<Post> findAllByMemberNotDeleted(Member member, Pageable pageable);
}
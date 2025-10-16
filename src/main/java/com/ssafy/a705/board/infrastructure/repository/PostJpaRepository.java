package com.ssafy.a705.board.infrastructure.repository;

import com.ssafy.a705.board.domain.entity.Post;
import com.ssafy.a705.board.domain.repository.PostRepository;
import com.ssafy.a705.member.domain.entity.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostJpaRepository extends JpaRepository<Post, Long>, PostRepository {

    @Override
    @Query("""
                SELECT p FROM Post p
                WHERE p.deletedAt IS NULL
                  AND (:cursorId IS NULL OR p.id < :cursorId)
                ORDER BY p.id DESC
            """)
    List<Post> findAllNotDeleted(
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    @Override
    @Query("SELECT p FROM Post p WHERE p.member = :member AND p.deletedAt IS NULL")
    Page<Post> findAllByMemberNotDeleted(Member member, Pageable pageable);
}
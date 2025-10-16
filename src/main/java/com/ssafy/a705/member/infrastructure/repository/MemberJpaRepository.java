package com.ssafy.a705.member.infrastructure.repository;

import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.member.domain.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberJpaRepository extends JpaRepository<Member, Long>, MemberRepository {

    @Override
    boolean existsByEmail(String email);

    @Override
    Optional<Member> findByEmailAndDeletedAtIsNull(String email);

    @Override
    Optional<Member> findByIdAndDeletedAtIsNull(Long id);

    @Override
    List<Member> findByEmailInAndDeletedAtIsNull(List<String> emails);

    @Override
    @Query("SELECT m FROM Member m WHERE m.nickname LIKE %:nickname% AND m.deletedAt IS NULL")
    List<Member> findAllByNicknameNotDeleted(@Param("nickname") String nickname);
    
}

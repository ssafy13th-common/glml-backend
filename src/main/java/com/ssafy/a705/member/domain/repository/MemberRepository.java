package com.ssafy.a705.member.domain.repository;

import com.ssafy.a705.member.domain.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByEmail(String email);

    Optional<Member> findByEmailAndDeletedAtIsNull(String email);

    Optional<Member> findByIdAndDeletedAtIsNull(Long id);

    List<Member> findByEmailInAndDeletedAtIsNull(List<String> emails);

    @Query("SELECT m FROM Member m WHERE m.nickname LIKE %:nickname% AND m.deletedAt IS NULL")
    List<Member> findAllByNicknameNotDeleted(@Param("nickname") String nickname);
}

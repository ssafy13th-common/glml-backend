package com.ssafy.a705.domain.member.repository;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.exception.MemberNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmailAndDeletedAtIsNull(String email);

    Optional<Member> findByIdAndDeletedAtIsNull(Long id);

    List<Member> findByEmailInAndDeletedAtIsNull(List<String> emails);

    @Query("SELECT m FROM Member m WHERE m.nickname LIKE %:nickname% AND m.deletedAt IS NULL")
    List<Member> findAllByNicknameNotDeleted(@Param("nickname") String nickname);

    default Member getByEmail(String email) {
        return findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    default List<Member> getAllByEmail(List<String> emails) {
        List<Member> members = findByEmailInAndDeletedAtIsNull(emails);
        if (members.isEmpty()) {
            throw new MemberNotFoundException();
        }
        return members;
    }

    default @NonNull Member getById(@NonNull Long id) {
        return findByIdAndDeletedAtIsNull(id).orElseThrow(MemberNotFoundException::new);
    }
}

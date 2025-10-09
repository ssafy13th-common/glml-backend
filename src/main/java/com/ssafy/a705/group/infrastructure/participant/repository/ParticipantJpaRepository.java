package com.ssafy.a705.group.infrastructure.participant.repository;

import com.ssafy.a705.group.domain.participant.entity.Participant;
import com.ssafy.a705.group.domain.participant.exception.ParticipantNotFoundException;
import com.ssafy.a705.group.domain.participant.repository.ParticipantRepository;
import com.ssafy.a705.group.presentation.participant.dto.response.ParticipantProfileRes;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipantJpaRepository extends JpaRepository<Participant, Long>, ParticipantRepository {

    @Override
    // 특정 그룹에 속한 멤버들
    @Query("SELECT p FROM Participant p "
            + "JOIN FETCH p.member m "
            + "WHERE p.group.id = :groupId "
            + "AND p.deletedAt IS NULL AND m.deletedAt IS NULL")
    List<Participant> findMembersByGroupId(@Param("groupId") Long groupId);

    @Override
    // 특정 그룹에 속한 멤버들의 프로필 이미지 링크
    @Query("SELECT new com.ssafy.a705.domain.group._participant.dto.response.ParticipantProfileRes(p.group.id, m.profileUrl) "
            + "FROM Participant p JOIN p.member m "
            + "WHERE p.group.id IN :groupIds "
            + "AND p.deletedAt IS NULL")
    List<ParticipantProfileRes> findProfilesByGroupIds(@Param("groupIds") List<Long> groupIds);

    @Override
    // participantsIds 목록으로 GroupMember 반환
    @Query("SELECT p FROM Participant p "
            + "WHERE p.id IN :participantIds "
            + "AND p.deletedAt IS NULL")
    List<Participant> findByIdInAndDeletedAtIsNull(
            @Param("participantIds") List<Long> participantIds);

    @Override
    // 그룹에 특정 멤버가 속해있는지 판단 - 중복 체크를 위함
    @Query("SELECT CASE WHEN count(p) > 0 THEN TRUE ELSE FALSE END "
            + "FROM Participant p "
            + "JOIN p.member m "
            + "JOIN p.group g "
            + "WHERE m.id = :memberId "
            + "AND g.id = :groupId "
            + "AND g.deletedAt IS NULL "
            + "AND m.deletedAt IS NULL "
            + "AND p.deletedAt IS NULL")
    boolean isParticipantExists(@Param("participantId") Long participantId,
            @Param("groupId") Long groupId);

    @Override
    // 그룹 id와 멤버 id로 그룹 멤버 검색 - 정보 수정, 삭제를 위함
    @Query("SELECT p FROM Participant p " +
            "JOIN FETCH p.member m " +
            "JOIN FETCH p.group g " +
            "WHERE m.id = :memberId " +
            "AND g.id = :groupId " +
            "AND p.deletedAt IS NULL " +
            "AND m.deletedAt IS NULL " +
            "AND g.deletedAt IS NULL")
    Optional<Participant> findParticipantByMemberIdAndGroupId(@Param("memberId") Long memberId,
            @Param("groupId") Long groupId);

    @Override
    // 그룹 id와 멤버 email로 그룹 멤버 검색 - 정보 수정, 삭제를 위함
    @Query("SELECT p FROM Participant p " +
            "JOIN FETCH p.member m " +
            "JOIN FETCH p.group g " +
            "WHERE m.email = :email " +
            "AND g.id = :groupId " +
            "AND p.deletedAt IS NULL " +
            "AND m.deletedAt IS NULL " +
            "AND g.deletedAt IS NULL")
    Optional<Participant> findParticipantByMemberEmailAndGroupId(@Param("email") String email,
            @Param("groupId") Long groupId);

    @Override
    // 그룹에 속한 멤버들의 email 집합 - 일괄적으로 받아서 한 번의 쿼리로 중복체크를 하기 위함
    @Query("SELECT p.member.id FROM Participant p "
            + "WHERE p.group.id = :groupId "
            + "AND p.member.email IN :emails "
            + "AND p.deletedAt IS NULL")
    Set<Long> findExistingMemberIdsByEmails(@Param("groupId") Long groupId,
            @Param("emails") List<String> emails);

    @Override
    default Participant getByMemberIdAndGroupId(Long memberId, Long groupId) {
        return findParticipantByMemberIdAndGroupId(memberId, groupId).orElseThrow(
                ParticipantNotFoundException::new);
    }

    @Override
    default Participant getByMemberEmailAndGroupId(Long groupId, String email) {
        return findParticipantByMemberEmailAndGroupId(email, groupId).orElseThrow(
                ParticipantNotFoundException::new);
    }
}

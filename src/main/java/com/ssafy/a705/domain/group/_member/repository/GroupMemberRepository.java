package com.ssafy.a705.domain.group._member.repository;

import com.ssafy.a705.domain.group._member.dto.response.GroupMemberProfileRes;
import com.ssafy.a705.domain.group._member.entity.GroupMember;
import com.ssafy.a705.domain.group._member.exception.GroupMemberNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    // 특정 그룹에 속한 멤버들
    @Query(value = "SELECT gm FROM GroupMember gm "
            + "JOIN FETCH gm.member m "
            + "WHERE gm.group.id = :groupId "
            + "AND gm.deletedAt IS NULL AND m.deletedAt IS NULL")
    List<GroupMember> findMembersByGroupId(@Param("groupId") Long groupId);

    // 특정 그룹에 속한 멤버들의 프로필 이미지 링크
    @Query("SELECT new com.ssafy.a705.domain.group._member.dto.response.GroupMemberProfileRes(gm.group.id, m.profileUrl) "
            + "FROM GroupMember gm JOIN gm.member m "
            + "WHERE gm.group.id IN :groupIds "
            + "AND gm.deletedAt IS NULL")
    List<GroupMemberProfileRes> findProfilesByGroupIds(@Param("groupIds") List<Long> groupIds);

    // GroupMemberId 목록으로 GroupMember 반환
    @Query("SELECT gm FROM GroupMember gm "
            + "WHERE gm.id IN :groupMemberIds "
            + "AND gm.deletedAt IS NULL")
    List<GroupMember> findGroupMembersByGroupMemberIds(
            @Param("groupMemberIds") List<Long> groupMemberIds);

    // 그룹에 특정 멤버가 속해있는지 판단 - 중복 체크를 위함
    @Query("SELECT CASE WHEN count(gm) > 0 THEN TRUE ELSE FALSE END "
            + "FROM GroupMember gm "
            + "JOIN gm.member m "
            + "JOIN gm.group g "
            + "WHERE m.id = :memberId "
            + "AND g.id = :groupId "
            + "AND g.deletedAt IS NULL "
            + "AND m.deletedAt IS NULL "
            + "AND gm.deletedAt IS NULL")
    boolean isGroupMemberExists(@Param("memberId") Long memberId,
            @Param("groupId") Long groupId);


    // 그룹 id와 멤버 id로 그룹 멤버 검색 - 정보 수정, 삭제를 위함
    @Query("SELECT gm FROM GroupMember gm " +
            "JOIN FETCH gm.member m " +
            "JOIN FETCH gm.group g " +
            "WHERE m.id = :memberId " +
            "AND g.id = :groupId " +
            "AND gm.deletedAt IS NULL " +
            "AND m.deletedAt IS NULL " +
            "AND g.deletedAt IS NULL")
    Optional<GroupMember> findGroupMemberByMemberIdAndGroupId(@Param("memberId") Long memberId,
            @Param("groupId") Long groupId);


    // 그룹 id와 멤버 email로 그룹 멤버 검색 - 정보 수정, 삭제를 위함
    @Query("SELECT gm FROM GroupMember gm " +
            "JOIN FETCH gm.member m " +
            "JOIN FETCH gm.group g " +
            "WHERE m.email = :email " +
            "AND g.id = :groupId " +
            "AND gm.deletedAt IS NULL " +
            "AND m.deletedAt IS NULL " +
            "AND g.deletedAt IS NULL")
    Optional<GroupMember> findGroupMemberByMemberEmailAndGroupId(@Param("email") String email,
            @Param("groupId") Long groupId);

    // 그룹에 속한 멤버들의 email 집합 - 일괄적으로 받아서 한 번의 쿼리로 중복체크를 하기 위함
    @Query("SELECT gm.member.id FROM GroupMember gm "
            + "WHERE gm.group.id = :groupId "
            + "AND gm.member.email IN :emails "
            + "AND gm.deletedAt IS NULL")
    Set<Long> findExistingMemberIdsByEmails(@Param("groupId") Long groupId,
            @Param("emails") List<String> emails);

    default GroupMember getByMemberIdAndGroupId(Long memberId, Long groupId) {
        return findGroupMemberByMemberIdAndGroupId(memberId, groupId).orElseThrow(
                GroupMemberNotFoundException::new);
    }

    default GroupMember getByMemberEmailAndGroupId(Long groupId, String email) {
        return findGroupMemberByMemberEmailAndGroupId(email, groupId).orElseThrow(
                GroupMemberNotFoundException::new);
    }
}

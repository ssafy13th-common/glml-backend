package com.ssafy.a705.group.domain.participant.repository;

import com.ssafy.a705.group.domain.participant.entity.Participant;
import com.ssafy.a705.group.presentation.participant.dto.response.ParticipantProfileRes;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.repository.query.Param;

public interface ParticipantRepository {
    Participant save(Participant participant);
    List<Participant> saveAll(List<Participant> participants);

    List<Participant> findMembersByGroupId(@Param("groupId") Long groupId);
    List<ParticipantProfileRes> findProfilesByGroupIds(@Param("groupIds") List<Long> groupIds);

    List<Participant> findByIdInAndDeletedAtIsNull(
            @Param("participantIds") List<Long> participantIds);

    boolean isParticipantExists(@Param("participantId") Long participantId,
            @Param("groupId") Long groupId);


    Optional<Participant> findParticipantByMemberIdAndGroupId(@Param("memberId") Long memberId,
            @Param("groupId") Long groupId);

    Optional<Participant> findParticipantByMemberEmailAndGroupId(@Param("email") String email,
            @Param("groupId") Long groupId);
    Set<Long> findExistingMemberIdsByEmails(@Param("groupId") Long groupId,
            @Param("emails") List<String> emails);

    Participant getByMemberIdAndGroupId(Long memberId, Long groupId);

    Participant getByMemberEmailAndGroupId(Long groupId, String email);
}

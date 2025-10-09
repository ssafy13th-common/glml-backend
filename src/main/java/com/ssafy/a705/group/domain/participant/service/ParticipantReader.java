package com.ssafy.a705.group.domain.participant.service;

import com.ssafy.a705.group.domain.participant.entity.Participant;
import com.ssafy.a705.group.presentation.participant.dto.response.ParticipantProfileRes;
import java.util.List;
import java.util.Set;

public interface ParticipantReader {
    List<Participant> findMembersByGroupId(Long groupId);
    List<Participant> findByIdInAndDeletedAtIsNull(List<Long> participantsId);
    List<ParticipantProfileRes> getParticipantProfiles(List<Long> groupIds);
    Participant getByMemberEmailAndGroupId(Long groupId, String email);
    Participant getByMemberIdAndGroupId(Long memberId, Long groupId);
    boolean isExists(Long groupId, Long memberId);
    Set<Long> getExistingEmails(Long groupId, List<String> emails);
    
}

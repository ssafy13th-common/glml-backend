package com.ssafy.a705.group.infrastructure.participant.service;

import com.ssafy.a705.group.domain.participant.entity.Participant;
import com.ssafy.a705.group.domain.participant.repository.ParticipantRepository;
import com.ssafy.a705.group.domain.participant.service.ParticipantReader;
import com.ssafy.a705.group.presentation.participant.dto.response.ParticipantProfileRes;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParticipantReaderImpl implements ParticipantReader {

    private final ParticipantRepository participantRepository;


    @Override
    public List<Participant> findMembersByGroupId(Long groupId) {
        return participantRepository.findMembersByGroupId(groupId);
    }

    @Override
    public List<Participant> findByIdInAndDeletedAtIsNull(List<Long> participantsId) {
        return participantRepository.findByIdInAndDeletedAtIsNull(participantsId);
    }

    @Override
    public List<ParticipantProfileRes> getParticipantProfiles(List<Long> groupIds) {
        return participantRepository.findProfilesByGroupIds(groupIds);
    }

    @Override
    public Participant getByMemberEmailAndGroupId(Long groupId, String email) {
        return participantRepository.getByMemberEmailAndGroupId(groupId, email);
    }

    @Override
    public Participant getByMemberIdAndGroupId(Long memberId, Long groupId) {
        return participantRepository.getByMemberIdAndGroupId(memberId, groupId);
    }

    @Override
    public boolean isExists(Long groupId, Long memberId) {
        return participantRepository.isParticipantExists(memberId, groupId);
    }

    @Override
    public Set<Long> getExistingEmails(Long groupId, List<String> emails) {
        return participantRepository.findExistingMemberIdsByEmails(groupId,
                emails);
    }
}

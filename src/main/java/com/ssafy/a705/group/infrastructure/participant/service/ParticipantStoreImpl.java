package com.ssafy.a705.group.infrastructure.participant.service;

import com.ssafy.a705.group.domain.participant.entity.Participant;
import com.ssafy.a705.group.domain.participant.repository.ParticipantRepository;
import com.ssafy.a705.group.domain.participant.service.ParticipantStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParticipantStoreImpl implements ParticipantStore {

    private final ParticipantRepository participantRepository;

    @Override
    public void saveParticipant(Participant participant) {
        participantRepository.save(participant);
    }

    @Override
    public void saveAllParticipants(List<Participant> participants) {
        participantRepository.saveAll(participants);
    }
}

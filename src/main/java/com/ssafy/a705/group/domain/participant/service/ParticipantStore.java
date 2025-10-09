package com.ssafy.a705.group.domain.participant.service;

import com.ssafy.a705.group.domain.participant.entity.Participant;
import java.util.List;

public interface ParticipantStore {
    void saveParticipant(Participant participant);
    void saveAllParticipants(List<Participant> participants);
}

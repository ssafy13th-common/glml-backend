package com.ssafy.a705.domain.group._participant.dto.response;

import com.ssafy.a705.domain.group._participant.entity.Participant;
import java.util.List;

public record ParticipantsRes(
        Long groupId,
        int participantsCount,
        List<ParticipantInfoRes> participantInfos
) {

    public static ParticipantsRes from(Long groupId, List<Participant> participants) {
        List<ParticipantInfoRes> participantInfos = participants.stream().map(ParticipantInfoRes::from)
                .toList();
        return new ParticipantsRes(groupId, participantInfos.size(), participantInfos);
    }
}

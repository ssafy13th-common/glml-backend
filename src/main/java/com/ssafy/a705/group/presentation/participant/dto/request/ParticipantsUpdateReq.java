package com.ssafy.a705.group.presentation.participant.dto.request;

import java.util.List;

public record ParticipantsUpdateReq(
        Long groupId,
        List<ParticipantUpdateReq> updateParticipants
) {

}

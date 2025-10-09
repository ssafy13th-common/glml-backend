package com.ssafy.a705.group.presentation.participant.dto.request;

public record ParticipantUpdateReq(
        Long participantId,
        int finalAmount,
        int lateFee
) {

}

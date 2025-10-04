package com.ssafy.a705.domain.group._participant.dto.request;

public record ParticipantUpdateReq(
        Long participantId,
        int finalAmount,
        int lateFee
) {

}

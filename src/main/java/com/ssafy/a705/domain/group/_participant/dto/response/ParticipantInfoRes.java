package com.ssafy.a705.domain.group._participant.dto.response;

import com.ssafy.a705.domain.group._participant.entity.Participant;
import com.ssafy.a705.domain.group._participant.entity.Role;

public record ParticipantInfoRes(
        Long participantId,
        Role role,
        String profileImageUrl,
        String nickname,
        int finalAmount,
        int lateFee
) {

    public static ParticipantInfoRes from(Participant groupMember) {
        return new ParticipantInfoRes(groupMember.getId(),
                groupMember.getRole(),
                groupMember.getMember().getProfileUrl(),
                groupMember.getMember().getNickname(),
                groupMember.getFinalCost(),
                groupMember.getLateFee());
    }
}

package com.ssafy.a705.domain.group._participant.dto.request;

import java.util.List;

public record ParticipantsReq(
        List<String> emails
) {

}

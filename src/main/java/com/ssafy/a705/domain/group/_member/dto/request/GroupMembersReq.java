package com.ssafy.a705.domain.group._member.dto.request;

import java.util.List;

public record GroupMembersReq(
        List<String> emails
) {

}

package com.ssafy.a705.group.presentation.group.dto.response;

import com.ssafy.a705.group.presentation.participant.dto.response.ParticipantProfileRes;
import com.ssafy.a705.group.domain.group.entity.Group;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record GroupsRes(
        int groupsCount,
        List<GroupListRes> groups
) {

    public static GroupsRes of(List<Group> groups, List<Long> groupIds,
            List<ParticipantProfileRes> profileDtos) {
        Map<Long, List<String>> profileMap = profileDtos.stream().collect(Collectors.groupingBy(
                ParticipantProfileRes::groupId,
                Collectors.mapping(ParticipantProfileRes::profileUrl, Collectors.toList())
        )); // 아이디 별 프로필 이미지 목록 분류

        List<GroupListRes> responses = groups.stream()
                .map(group -> GroupListRes.from(group,
                        profileMap.getOrDefault(group.getId(), List.of()))).toList(); // 데이터 매핑
        return new GroupsRes(responses.size(), responses);
    }
}

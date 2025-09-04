package com.ssafy.a705.domain.group.dto.response;

import com.ssafy.a705.domain.group._member.dto.response.GroupMemberProfileRes;
import com.ssafy.a705.domain.group.entity.Group;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record GroupsRes(
        int groupsCount,
        List<GroupListRes> groups
) {

    public static GroupsRes of(List<Group> groups, List<Long> groupIds,
            List<GroupMemberProfileRes> profileDtos) {
        Map<Long, List<String>> profileMap = profileDtos.stream().collect(Collectors.groupingBy(
                GroupMemberProfileRes::groupId,
                Collectors.mapping(GroupMemberProfileRes::profileUrl, Collectors.toList())
        )); // 아이디 별 프로필 이미지 목록 분류

        List<GroupListRes> responses = groups.stream()
                .map(group -> GroupListRes.from(group,
                        profileMap.getOrDefault(group.getId(), List.of()))).toList(); // 데이터 매핑
        return new GroupsRes(responses.size(), responses);
    }
}

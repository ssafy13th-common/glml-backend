package com.ssafy.a705.domain.group._member.controller;

import com.ssafy.a705.domain.group._member.dto.request.GroupMembersReq;
import com.ssafy.a705.domain.group._member.dto.request.GroupMembersUpdateReq;
import com.ssafy.a705.domain.group._member.dto.response.GroupMembersRes;
import com.ssafy.a705.domain.group._member.service.GroupMemberService;
import com.ssafy.a705.domain.group.service.GroupAggregateService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class GroupMemberRestController {

    private final GroupMemberService groupMemberService;        // group 정보를 조회할 필요가 없는 경우에는 groupMemberService만 사용
    private final GroupAggregateService groupAggregateService;  // group 정보, member 정보를 조회할 필요가 있는 경우 groupAggregateService

    @GetMapping("/{group-id}/members")
    public ResponseEntity<ApiResponse<GroupMembersRes>> getGroupMembers(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        log.info("멤바 조회");
        GroupMembersRes groupMembers = groupMemberService.getGroupMembers(groupId,
                customUserDetails);
        return ApiResponse.ok(groupMembers);
    }


    @PostMapping("/{group-id}/members")
    public ResponseEntity<ApiResponse<Void>> createGroupMember(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody GroupMembersReq request) {
        groupAggregateService.createGroupMembers(groupId, request.emails(), customUserDetails);
        return ApiResponse.create();
    }

    @PutMapping("/{group-id}/members")
    public ResponseEntity<ApiResponse<GroupMembersRes>> updateGroupMember(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody GroupMembersUpdateReq request
    ) {
        GroupMembersRes groupMembersRes = groupMemberService.updateGroupMembers(request,
                customUserDetails);
        return ApiResponse.ok(groupMembersRes);
    }

    @DeleteMapping("/{group-id}/members")
    public ResponseEntity<ApiResponse<Void>> deleteGroupMember(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody Map<String, String> email
    ) {
        groupAggregateService.deleteGroupMember(groupId, email.get("email"), customUserDetails);
        return ApiResponse.ok();
    }
}

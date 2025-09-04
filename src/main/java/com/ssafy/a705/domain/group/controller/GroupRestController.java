package com.ssafy.a705.domain.group.controller;

import com.ssafy.a705.domain.group.dto.request.GroupReq;
import com.ssafy.a705.domain.group.dto.request.GroupUpdateReq;
import com.ssafy.a705.domain.group.dto.response.GroupGatheringRes;
import com.ssafy.a705.domain.group.dto.response.GroupInfoRes;
import com.ssafy.a705.domain.group.dto.response.GroupsRes;
import com.ssafy.a705.domain.group.service.GroupAggregateService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class GroupRestController {

    private final GroupAggregateService groupAggregateService;

    @GetMapping("/{group-id}")
    public ResponseEntity<ApiResponse<GroupInfoRes>> getGroup(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        GroupInfoRes response = groupAggregateService.getGroup(groupId, customUserDetails);
        return ApiResponse.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<GroupsRes>> getGroups(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        GroupsRes response = groupAggregateService.getGroups(customUserDetails);
        return ApiResponse.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GroupInfoRes>> createGroup(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid GroupReq request
    ) {
        GroupInfoRes groupInfoRes = groupAggregateService.createGroupWithAdminAndMembers(request,
                customUserDetails);
        return ApiResponse.create(groupInfoRes);
    }

    @PutMapping("/{group-id}")
    public ResponseEntity<ApiResponse<GroupInfoRes>> updateGroup(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("group-id") Long groupId,
            @RequestBody @Valid GroupUpdateReq request
    ) {
        GroupInfoRes groupInfoRes = groupAggregateService.updateGroup(groupId, request,
                customUserDetails);
        return ApiResponse.ok(groupInfoRes);
    }

    @DeleteMapping("/{group-id}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("group-id") Long groupId
    ) {
        groupAggregateService.deleteGroup(groupId, customUserDetails);
        return ApiResponse.ok();
    }


    @GetMapping("/{group-id}/gathering")
    public ResponseEntity<ApiResponse<GroupGatheringRes>> getGatheringInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("group-id") Long groupId
    ) {
        GroupGatheringRes gatheringRes = groupAggregateService.getGroupGatheringInfo(groupId,
                customUserDetails);
        return ApiResponse.ok(gatheringRes);
    }
}

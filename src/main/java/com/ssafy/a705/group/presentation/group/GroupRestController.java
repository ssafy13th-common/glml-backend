package com.ssafy.a705.group.presentation.group;

import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.group.application.group.GroupApplicationService;
import com.ssafy.a705.group.presentation.group.dto.request.GroupReq;
import com.ssafy.a705.group.presentation.group.dto.request.GroupUpdateReq;
import com.ssafy.a705.group.presentation.group.dto.response.GroupGatheringRes;
import com.ssafy.a705.group.presentation.group.dto.response.GroupInfoRes;
import com.ssafy.a705.group.presentation.group.dto.response.GroupsRes;
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

    private final GroupApplicationService groupService;

    @PostMapping
    public ResponseEntity<ApiResponse<GroupInfoRes>> createGroup(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid GroupReq request
    ) {
        GroupInfoRes response = groupService.createGroup(request,
                userDetails);
        return ApiResponse.create(response);
    }

    @GetMapping("/{group-id}")
    public ResponseEntity<ApiResponse<GroupInfoRes>> getGroup(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        GroupInfoRes response = groupService.getGroup(groupId, userDetails);
        return ApiResponse.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<GroupsRes>> getGroups(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        GroupsRes response = groupService.getGroups(userDetails);
        return ApiResponse.ok(response);
    }

    @PutMapping("/{group-id}")
    public ResponseEntity<ApiResponse<GroupInfoRes>> updateGroup(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("group-id") Long groupId,
            @RequestBody @Valid GroupUpdateReq request
    ) {
        GroupInfoRes response = groupService.updateGroup(groupId, request,
                userDetails);
        return ApiResponse.ok(response);
    }

    @DeleteMapping("/{group-id}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("group-id") Long groupId
    ) {
        groupService.deleteGroup(groupId, userDetails);
        return ApiResponse.ok();
    }


    @GetMapping("/{group-id}/gathering")
    public ResponseEntity<ApiResponse<GroupGatheringRes>> getGatheringInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("group-id") Long groupId
    ) {
        GroupGatheringRes response = groupService.getGroupGatheringInfo(groupId,
                userDetails);
        return ApiResponse.ok(response);
    }
}

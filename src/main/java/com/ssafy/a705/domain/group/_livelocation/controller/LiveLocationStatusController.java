package com.ssafy.a705.domain.group._livelocation.controller;

import com.ssafy.a705.domain.group._livelocation.service.LiveLocationStatusService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/group/{group-id}/live-location/status")
public class LiveLocationStatusController {

    private final LiveLocationStatusService liveLocationStatusService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> enable(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        liveLocationStatusService.enableStatus(groupId, userDetails);
        return ApiResponse.ok();
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> disable(
            @PathVariable("group-id") Long groupId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        liveLocationStatusService.disableStatus(groupId, userDetails);
        return ApiResponse.ok();
    }
}
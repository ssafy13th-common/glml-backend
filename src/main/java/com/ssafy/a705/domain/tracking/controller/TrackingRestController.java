package com.ssafy.a705.domain.tracking.controller;

import com.ssafy.a705.domain.tracking.dto.request.TrackingCreateReq;
import com.ssafy.a705.domain.tracking.dto.request.TrackingUpdateReq;
import com.ssafy.a705.domain.tracking.dto.response.TrackingCreateRes;
import com.ssafy.a705.domain.tracking.dto.response.TrackingDetailRes;
import com.ssafy.a705.domain.tracking.dto.response.TrackingInfosRes;
import com.ssafy.a705.domain.tracking.service.TrackingService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trackings")
public class TrackingRestController {

    private final TrackingService trackingService;

    @PostMapping
    public ResponseEntity<ApiResponse<TrackingCreateRes>> createTracking(
            @RequestBody @Valid TrackingCreateReq trackingCreateReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TrackingCreateRes res = trackingService.createTracking(trackingCreateReq, userDetails);
        return ApiResponse.ok(res);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<TrackingInfosRes>> getTrackingInfos(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TrackingInfosRes trackingInfosRes = trackingService.getTrackingInfos(userDetails);
        return ApiResponse.ok(trackingInfosRes);
    }

    @GetMapping("/{tracking-id}")
    public ResponseEntity<ApiResponse<TrackingDetailRes>> getTrackingDetail(
            @PathVariable("tracking-id") String trackingId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        TrackingDetailRes trackingDetail = trackingService.getTrackingDetail(trackingId,
                userDetails);
        return ApiResponse.ok(trackingDetail);
    }


    @PatchMapping("/{tracking-id}")
    public ResponseEntity<ApiResponse<Void>> updateTracking(
            @PathVariable("tracking-id") String trackingId,
            @RequestBody TrackingUpdateReq trackingUpdateReq,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        trackingService.updateTracking(trackingId, trackingUpdateReq, userDetails);
        return ApiResponse.ok();

    }

    @DeleteMapping("/{tracking-id}")
    public ResponseEntity<ApiResponse<Void>> deleteTracking(
            @PathVariable("tracking-id") String trackingId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        trackingService.deleteTracking(trackingId, userDetails);
        return ApiResponse.ok();
    }
}

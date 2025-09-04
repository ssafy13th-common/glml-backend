package com.ssafy.a705.domain.location._color.controller;


import com.ssafy.a705.domain.location._color.dto.request.ColorUpdateReq;
import com.ssafy.a705.domain.location._color.dto.response.MapColorRes;
import com.ssafy.a705.domain.location._color.service.LocationColorService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/locations")
public class LocationColorRestController {

    private final LocationColorService colorService;

    @PatchMapping("/{location-code}")
    public ResponseEntity<ApiResponse<Void>> updateLocationColor(
            @RequestBody @Valid ColorUpdateReq colorSetReq,
            @PathVariable("location-code") Integer locationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        colorService.updateLocationColor(locationId, colorSetReq, userDetails);
        return ApiResponse.ok();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MapColorRes>> getLocationColors(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MapColorRes res = colorService.getLocationColors(userDetails);
        return ApiResponse.ok(res);
    }

}
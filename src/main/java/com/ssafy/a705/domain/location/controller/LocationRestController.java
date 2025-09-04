package com.ssafy.a705.domain.location.controller;

import com.ssafy.a705.domain.location.service.LocationService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/locations")
public class LocationRestController {

    private final LocationService locationService;

    @PostMapping("/infos")
    public ResponseEntity<ApiResponse<Void>> saveLocations(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        locationService.saveLocations();
        return ApiResponse.ok();
    }

}

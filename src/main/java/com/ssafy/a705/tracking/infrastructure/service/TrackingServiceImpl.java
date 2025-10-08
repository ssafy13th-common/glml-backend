package com.ssafy.a705.tracking.infrastructure.service;

import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.tracking.domain.entity.Tracking;
import com.ssafy.a705.tracking.domain.service.TrackingReader;
import com.ssafy.a705.tracking.domain.service.TrackingService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    private final TrackingReader trackingReader;

    @Override
    public Tracking getTracking(String trackingId, CustomUserDetails userDetails) {
        Tracking tracking = trackingReader.getTracking(trackingId);
        checkUserHaveTracking(userDetails, tracking);
        return tracking;
    }

    private void checkUserHaveTracking(CustomUserDetails userDetails, Tracking tracking) {
        if (Objects.equals(userDetails.getId(), tracking.getUserId())) {
            return;
        }
        throw new ForbiddenException("트래킹 접근");
    }

}

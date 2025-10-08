package com.ssafy.a705.tracking.domain.service;

import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.tracking.domain.entity.Tracking;

public interface TrackingService {

    Tracking getTracking(String trackingId, CustomUserDetails userDetails);

}

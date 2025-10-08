package com.ssafy.a705.tracking.domain.service;

import com.ssafy.a705.tracking.domain.entity.Tracking;

public interface TrackingReader {

    Tracking getTracking(String trackingId);
}

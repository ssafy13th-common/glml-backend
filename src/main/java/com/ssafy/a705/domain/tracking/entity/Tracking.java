package com.ssafy.a705.domain.tracking.entity;

import com.ssafy.a705.domain.tracking.dto.request.TrackingCreateReq;
import com.ssafy.a705.domain.tracking.dto.request.TrackingUpdateReq;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "trackings")
public class Tracking {

    @Id
    private String id;

    private Long userId;

    private List<TrackPoint> tracks;

    private List<String> images;

    private final LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    private Tracking(Long userId, List<TrackPoint> tracks) {
        this.userId = userId;
        this.tracks = tracks;
        this.images = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public static Tracking from(TrackingCreateReq trackingReq, CustomUserDetails userDetails) {
        return new Tracking(userDetails.getId(), trackingReq.tracks());
    }

    public void update(TrackingUpdateReq trackingUpdateReq) {
        this.modifiedAt = LocalDateTime.now();
        this.images = trackingUpdateReq.images();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
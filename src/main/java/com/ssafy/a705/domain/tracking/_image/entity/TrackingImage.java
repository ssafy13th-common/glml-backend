package com.ssafy.a705.domain.tracking._image.entity;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "tracking_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrackingImage extends BaseEntity {

    @Id
    @Comment("트래킹 이미지 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("사진 URL")
    @Column(nullable = false)
    private String imageUrl;

    @Comment("트래킹 id")
    @Column(nullable = false, unique = true)
    private String trackingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private TrackingImage(String imageUrl, String trackingId, Member member) {
        this.imageUrl = imageUrl;
        this.trackingId = trackingId;
        this.member = member;
    }

    public static TrackingImage of(String imageUrl, String trackingId, Member member) {
        return new TrackingImage(imageUrl, trackingId, member);
    }

    public void updateImage(String imageUrl) {
        this.restore();
        this.imageUrl = imageUrl;
    }

    public void deleteImage() {
        this.delete(LocalDateTime.now());
    }

}

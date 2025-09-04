package com.ssafy.a705.domain.location._color.entity;

import com.ssafy.a705.domain.location._color.dto.request.ColorUpdateReq;
import com.ssafy.a705.domain.location.entity.Location;
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
@Table(name = "location_colors")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationColor extends BaseEntity {

    @Id
    @Comment("지역별 색 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("지역 색")
    @Column(nullable = false, length = 6)
    private String color;

    @Comment("투명도")
    @Column(nullable = false, length = 2)
    private String transparency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private LocationColor(String color, Member member, Location location) {
        this.color = color;
        this.transparency = "66";
        this.member = member;
        this.location = location;
    }

    public static LocationColor of(String color, Member member, Location location) {
        return new LocationColor(color, member, location);
    }

    public void updateColor(ColorUpdateReq colorUpdateReq) {
        this.color = colorUpdateReq.color();
    }

    public void updateTransparency(String transparency) {
        this.transparency = transparency;
    }

    public void deleteColor() {
        this.delete(LocalDateTime.now());
    }
}

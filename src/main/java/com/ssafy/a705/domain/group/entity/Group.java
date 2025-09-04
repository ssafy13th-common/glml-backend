package com.ssafy.a705.domain.group.entity;

import com.ssafy.a705.domain.group.dto.request.GroupReq;
import com.ssafy.a705.domain.group.dto.request.GroupUpdateReq;
import com.ssafy.a705.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "`groups`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends BaseEntity {

    @Id
    @Comment("그룹 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("그룹 명")
    @Column(nullable = false)
    private String name;

    @Comment("그룹 상태")
    @Column(nullable = false, length = 11)
    @Enumerated(EnumType.STRING)
    private GroupStatus groupStatus;

    @Comment("그룹 설명")
    @Column(length = 100)
    private String summary;

    @Comment("모임 시간")
    private LocalDateTime gatheringTime;

    @Comment("모임 장소")
    private String gatheringLocation;

    @Comment("모임 장소 위도")
    private Double locationLatitude;

    @Comment("모임 장소 경도")
    private Double locationLongitude;

    @Comment("채팅방 Id")
    private String chatRoomId;

    @Comment("여행 시작일")
    private LocalDate startAt;

    @Comment("여행 종료일")
    private LocalDate endAt;

    @Comment("분당 지각비")
    private int feePerMinute;

    private Group(String name, String summary, LocalDateTime gatheringTime,
            String gatheringLocation, LocalDate startAt, LocalDate endAt, int feePerMinute) {
        this.name = name;
        this.groupStatus = GroupStatus.TO_DO;
        this.summary = summary;
        this.gatheringTime = gatheringTime;
        this.gatheringLocation = gatheringLocation;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.startAt = startAt;
        this.endAt = endAt;
        this.feePerMinute = feePerMinute;
    }

    private Group(String name,  String summary) {
        this.name = name;
        this.groupStatus = GroupStatus.TO_DO;
        this.summary = summary;
    }

    public static Group from(GroupReq groupReq) {
        return new Group(groupReq.name(), groupReq.summary());
    }

    public void update(GroupUpdateReq groupReq) {
        this.name = groupReq.name();
        this.summary = groupReq.summary();
        this.gatheringTime = groupReq.gatheringTime();
        this.gatheringLocation = groupReq.gatheringLocation();
        this.locationLatitude = groupReq.locationLatitude();
        this.locationLongitude = groupReq.locationLongitude();
        this.startAt = groupReq.startAt();
        this.endAt = groupReq.endAt();
        this.feePerMinute = groupReq.feePerMinute();
    }

    public void deleteGroup() {
        this.delete(LocalDateTime.now());
    }

    public void updateChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}

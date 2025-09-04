package com.ssafy.a705.domain.diary.entity;

import com.ssafy.a705.domain.diary._image.entity.DiaryImage;
import com.ssafy.a705.domain.diary.dto.request.DiaryCreateReq;
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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "diaries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseEntity {

    @Id
    @Comment("다이어리 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("시작일")
    @Column(nullable = false)
    private LocalDate startedAt;

    @Comment("종료일")
    @Column(nullable = false)
    private LocalDate endedAt;

    @Lob
    @Comment("다이어리 본문")
    private String content;

    @Comment("다이어리 이미지 url")
    @OneToMany(mappedBy = "diary", fetch = FetchType.LAZY)
    private List<DiaryImage> diaryImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private Diary(LocalDate startedAt, LocalDate endedAt, String content, Member member,
            Location location) {
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.content = content;
        this.member = member;
        this.location = location;
    }

    public static Diary from(DiaryCreateReq diaryReq, Member member, Location location) {
        return new Diary(diaryReq.startedAt(), diaryReq.endedAt(), diaryReq.content(), member,
                location);
    }

    public void update(LocalDate started_at, LocalDate ended_at, String content) {
        this.startedAt = started_at;
        this.endedAt = ended_at;
        this.content = content;
    }

    public void deleteDiary() {
        this.delete(LocalDateTime.now());
    }

}

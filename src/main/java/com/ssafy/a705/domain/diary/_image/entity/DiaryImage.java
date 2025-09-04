package com.ssafy.a705.domain.diary._image.entity;

import com.ssafy.a705.domain.diary.entity.Diary;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "diary_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryImage extends BaseEntity {

    @Id
    @Comment("다이어리 이미지 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Comment("사진 URL")
    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    private DiaryImage(String imageUrl, Diary diary) {
        this.imageUrl = imageUrl;
        this.diary = diary;
    }

    public static DiaryImage of(String imageUrl, Diary diary) {
        return new DiaryImage(imageUrl, diary);
    }

    public void deleteDiaryImage() {
        this.delete(LocalDateTime.now());
    }

}

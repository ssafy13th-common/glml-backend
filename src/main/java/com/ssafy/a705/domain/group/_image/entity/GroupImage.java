package com.ssafy.a705.domain.group._image.entity;

import com.ssafy.a705.domain.group._participant.entity.Participant;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupImage extends BaseEntity {

    @Id
    @Comment("그룹 이미지 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Comment("이미지 url")
    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    private GroupImage(String url, Participant participant) {
        this.url = url;
        this.participant = participant;
    }

    public static GroupImage of(String url, Participant groupMember) {
        return new GroupImage(url, groupMember);
    }

    public void deleteImage() {
        this.delete(LocalDateTime.now());
    }
}

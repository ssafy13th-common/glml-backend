package com.ssafy.a705.domain.group._image.entity;

import com.ssafy.a705.domain.group._member.entity.GroupMember;
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
@Table(name = "group_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupImage extends BaseEntity {

    @Id
    @Comment("그룹 이미지 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("이미지 url")
    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_member_id", nullable = false)
    private GroupMember groupMember;

    private GroupImage(String imageUrl, GroupMember groupMember) {
        this.imageUrl = imageUrl;
        this.groupMember = groupMember;
    }

    public static GroupImage of(String imageUrl, GroupMember groupMember) {
        return new GroupImage(imageUrl, groupMember);
    }

    public void deleteImage() {
        this.delete(LocalDateTime.now());
    }
}

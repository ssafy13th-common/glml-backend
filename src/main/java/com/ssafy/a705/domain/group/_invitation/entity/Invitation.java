package com.ssafy.a705.domain.group._invitation.entity;

import com.ssafy.a705.domain.group.entity.Group;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation extends BaseEntity {

    @Id
    @Comment("초대 알림 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("초대 알림 제목")
    @Column(length = 100)
    private String title;

    @Comment("초대 알림 본문")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Invitation(String title, String content, Group group, Member member) {
        this.title = title;
        this.content = content;
        this.group = group;
        this.member = member;
    }
}

package com.ssafy.a705.domain.group._member.entity;

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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant extends BaseEntity {

    @Id
    @Comment("그룹 멤버 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("최종 정산 금액")
    @Column(nullable = false)
    private int finalCost;

    @Comment("지각비")
    @Column(nullable = false)
    private int lateFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Comment("그룹 멤버 역할")
    @Column(nullable = false)
    private Role role;

    private Participant(Group group, Member member,
            Role role) {
        this.finalCost = 0;
        this.lateFee = 0;
        this.group = group;
        this.member = member;
        this.role = role;
    }

    public static Participant of(Group group, Member member,
            Role role) {
        return new Participant(group, member, role);
    }

    public void updateAmount(int finalAmount, int lateFee) {
        this.finalCost = finalAmount;
        this.lateFee = lateFee;
    }

    public void upgradeToAdmin() {
        this.role = Role.LEADER;
    }

    public void deleteParticipant() {
        this.delete(LocalDateTime.now());
    }


}

package com.ssafy.a705.domain.group._memo.entity;

import com.ssafy.a705.domain.group._member.entity.GroupMember;
import com.ssafy.a705.global.common.BaseEntity;
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
@Table(name = "group_memos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMemo extends BaseEntity {

    @Id
    @Comment("그룹 메모 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("메모 내용")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_member_id", nullable = false)
    private GroupMember groupMember;

    private GroupMemo(String content, GroupMember groupMember) {
        this.content = content;
        this.groupMember = groupMember;
    }

    public static GroupMemo of(String content, GroupMember groupMember) {
        return new GroupMemo(content, groupMember);
    }

    public void updateMemo(String content) {
        this.content = content;
    }

    public void deleteMemo() {
        this.delete(LocalDateTime.now());
    }
}

package com.ssafy.a705.board.domain.entity;

import com.ssafy.a705.board.presentation.dto.request.ReplyRegisterReq;
import com.ssafy.a705.board.presentation.dto.request.ReplyUpdateReq;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.global.common.BaseEntity;
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
public class Reply extends BaseEntity {

    @Id
    @Comment("댓글 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("본문")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Reply parent;

    private Reply(String content, Post post, Member member,
            Reply parent) {
        this.content = content;
        this.post = post;
        this.member = member;
        this.parent = parent;
    }

    public static Reply from(ReplyRegisterReq replyReq, Post post,
            Member member, Reply parent) {
        return new Reply(replyReq.content(), post, member, parent);
    }

    public void updateContent(ReplyUpdateReq replyUpdateReq) {
        this.content = replyUpdateReq.content();
    }

    public void deleteReply() {
        this.delete(LocalDateTime.now());
    }
}

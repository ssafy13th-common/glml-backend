package com.ssafy.a705.domain.board._post.entity;

import com.ssafy.a705.domain.board._post.dto.request.PostDetailReq;
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
import org.hibernate.annotations.Formula;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @Comment("게시글 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("제목")
    private String title;

    @Comment("본문")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Formula("(SELECT COUNT(*) FROM reply c WHERE c.post_id = id AND c.deleted_at IS NULL)")
    private int replyCount;

    private Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public static Post from(PostDetailReq boardReq, Member member) {
        return new Post(boardReq.title(), boardReq.content(), member);
    }

    public void update(PostDetailReq boardReq) {
        this.title = boardReq.title();
        this.content = boardReq.content();
    }

    public void deletePost() {
        this.delete(LocalDateTime.now());
    }
}

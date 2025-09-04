package com.ssafy.a705.domain.board._comment.entity;

import com.ssafy.a705.domain.board._comment.dto.request.CommentRegisterReq;
import com.ssafy.a705.domain.board._comment.dto.request.CommentUpdateReq;
import com.ssafy.a705.domain.board.entity.CompanyBoard;
import com.ssafy.a705.domain.member.entity.Member;
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
@Table(name = "company_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyComment extends BaseEntity {

    @Id
    @Comment("댓글 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("본문")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private CompanyBoard companyBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CompanyComment parent;

    private CompanyComment(String content, CompanyBoard companyBoard, Member member,
            CompanyComment parent) {
        this.content = content;
        this.companyBoard = companyBoard;
        this.member = member;
        this.parent = parent;
    }

    public static CompanyComment from(CommentRegisterReq commentReq, CompanyBoard board,
            Member member, CompanyComment parent) {
        return new CompanyComment(commentReq.content(), board, member, parent);
    }

    public void updateComment(CommentUpdateReq commentUpdateReq) {
        this.content = commentUpdateReq.content();
    }

    public void deleteComment() {
        this.delete(LocalDateTime.now());
    }
}

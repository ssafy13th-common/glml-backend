package com.ssafy.a705.domain.board.entity;

import com.ssafy.a705.domain.board.dto.request.BoardDetailReq;
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
import org.hibernate.annotations.Formula;

@Entity
@Getter
@Table(name = "company_boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyBoard extends BaseEntity {

    @Id
    @Comment("동행게시판 식별자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("제목")
    private String title;

    @Comment("본문")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Formula("(SELECT COUNT(*) FROM company_comments c WHERE c.board_id = id AND c.deleted_at IS NULL)")
    private int commentCount;

    private CompanyBoard(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public static CompanyBoard from(BoardDetailReq boardReq, Member member) {
        return new CompanyBoard(boardReq.title(), boardReq.content(), member);
    }

    public void update(BoardDetailReq boardReq) {
        this.title = boardReq.title();
        this.content = boardReq.content();
    }

    public void deleteBoard() {
        this.delete(LocalDateTime.now());
    }
}
